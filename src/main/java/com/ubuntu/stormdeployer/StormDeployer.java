/**
 *
 */
package com.ubuntu.stormdeployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.apache.maven.cli.MavenCli;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

/**
 * @author maarten Undeploy storm topologies
 */
public class StormDeployer {

    public void execute(String command, PrintStream log) throws Exception {
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();

        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                log.append(line + "\n");
            }
        } finally {
            reader.close();
        }

    }

    public void wget(URL url, File destination) throws Exception {

        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(destination);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    public void git(String giturl, File directory, PrintStream out) throws Exception {
        execute("git clone " + giturl + " " + directory.getAbsolutePath(), out);

    }

    public void deploy(String command, Topology topology, PrintStream out) throws Exception {
        String name = topology.getName();
        String jar = topology.getJar();
        String packaging = topology.getPackaging();
        String repo = topology.getRepository();
        String topologyclass = topology.getTopologyclass();
        String beforePackage = topology.getScriptbeforepackaging();
        String beforeDeploy = topology.getScriptbeforedeploying();
        List<DataSource> dss = topology.getDatasources();

        // choise target folder
        File target = new File("/tmp/stormdeployertmp");
        if (!target.exists()) {
            
            out.append("Creating /tmp/stormdeployertmp \n");
            
            target.mkdir();
        }

        File topologyDir = new File(target.getAbsolutePath() + "/" + name);
        // Did we deploy this topology already?
        if (!topologyDir.exists()) {
            out.append("Topology not yet deployed.\n");
            topologyDir.mkdir();
            // download from git
            git(repo, topologyDir, out);

            // Run before packaging script
            if (beforePackage != null) {
                out.append("Run before packaging script.\n");
                File beforePackageDir = new File(topologyDir.getAbsolutePath() + "/package");
                beforePackageDir.mkdir();
                File packageScript = new File(beforePackageDir.getAbsolutePath() + "/script");
                wget(new URL(beforePackage), packageScript);
                execute("chmod u+x " + packageScript.getAbsolutePath(), out);
                execute(packageScript.getAbsolutePath(), out);
            }

            if (dss != null) {
                out.append("Applying datasources.\n");
                for (DataSource ds : dss) {
                    applyDataSource(ds, topologyDir, out);
                }
            }

            // Todo implement alternative packaging and subdirectory support for Maven
            MavenCli cli = new MavenCli();
            cli.doMain(new String[]{"package"}, topologyDir.getAbsolutePath(), out, out);

            // Run before deploying script
            if (beforeDeploy != null) {
                out.append("Running before deploy script.\n");
                File beforeDeployDir = new File(topologyDir.getAbsolutePath() + "/deploy");
                beforeDeployDir.mkdir();
                File deployScript = new File(beforeDeployDir.getAbsolutePath() + "/script");
                wget(new URL(beforeDeploy), deployScript);
                execute("chmod u+x " + deployScript.getAbsolutePath(), out);
                execute(deployScript.getAbsolutePath(), out);
            }

            // Deploy the topology
            deploy(command, topologyDir.getAbsolutePath() + "/target/" + jar, topologyclass, name, out);
        }
    }

    public void applyDataSource(DataSource ds, File topologyDir,
            PrintStream out) throws Exception {
        List<Parameter> params = ds.getParameters();
        String scriptURL = ds.getScript();
        String type = ds.getType();
        File dsdir = new File(topologyDir.getAbsolutePath() + "/datasources");
        dsdir.mkdir();
        File dir = new File(topologyDir.getAbsolutePath() + "/datasources/" + type);
        dir.mkdir();
        File file = new File(topologyDir.getAbsolutePath() + "/datasources/" + type + "/script");
        wget(new URL(scriptURL), file);
        execute("chmod u+x " + file, out);
        StringBuffer sb = new StringBuffer();
        for (Parameter param : params) {
            sb.append(param.getName());
            sb.append("=");
            sb.append(param.getValue());
            sb.append(" ");

        }
        execute(file + " " + sb.toString(), out);
    }

    public void deploy(String deployment, String jar, String topologyClass, String topologyName, PrintStream out) throws Exception {
        out.append("Deploying:" + deployment + " " + jar + " " + topologyClass + " " + topologyName + "\n");
        execute(deployment + " " + jar + " " + topologyClass + " " + topologyName, out);
    }

    public List<Topology> readTopologies(String file) throws Exception {
        StringBuffer deployer = new StringBuffer();
        FileReader fr = new FileReader(file);
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(fr);

            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                deployer.append(sCurrentLine);
                deployer.append("\n");
            }

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {

            }
        }

        TopologiesConstructor constructor = new TopologiesConstructor(Topologies.class);
        Loader loader = new Loader(constructor);
        Yaml yaml = new Yaml(loader);
        Topologies topologies = (Topologies) yaml.load(deployer.toString());
        return topologies.getTopology();
    }

    /**
     * @param args
     * @throws Exception When deployment can not be done.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Exception("deployer file is missing. Usage: deployerURL (debugLogFile)");
        }
        PrintStream out = System.out;
        if (args.length > 1) {
            out = new PrintStream(args[1]);
        }
        StormDeployer sd = new StormDeployer();
        File stormFile = new File("/tmp/stormdeploy" + System.nanoTime());
        sd.wget(new URL(args[0]), stormFile);
        //sd.wget(new URL("https://raw.githubusercontent.com/xannz/storm-projects/master/WordCount/WordCount.storm"), stormFile);

        for (Topology topology : sd.readTopologies(stormFile.getAbsolutePath())) {
            out.append("Deploying topology:" + topology.getName());
            sd.deploy("/opt/storm/latest/bin/storm jar", topology, out);
        }
    }

}
