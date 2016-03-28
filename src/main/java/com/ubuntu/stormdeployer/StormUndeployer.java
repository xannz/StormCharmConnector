/**
 * 
 */
package com.ubuntu.stormdeployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

import org.apache.maven.cli.MavenCli;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;


/**
 * @author maarten
 * Deploy storm
 */
public class StormUndeployer {

	public void execute(String command, PrintStream log) throws Exception
	{
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
     
        BufferedReader reader = null;
        try{
        	
        
           reader=  new BufferedReader(new InputStreamReader(p.getInputStream()));
        
	        String line = "";			
	        while ((line = reader.readLine())!= null) {
	        	log.append(line + "\n");
	        }
        } finally {
        	reader.close();
        }
		
	}
	

	
	public void undeploy(String undeployment,String directory, String topology, PrintStream out) throws Exception
	{
		execute(undeployment + " " + topology, out);
        execute("rm -rf "+directory,out);
	}
	/**
	 * @param args
	 * @throws Exception When deployment can not be done.
	 */
	public static void main(String[] args) throws Exception {
		if(args.length<1)
			throw new Exception("topology name to undeploy is missing. Usage: topology (debugLogFile)");
		PrintStream out = System.out;
		if(args.length>1)
		{
			out = new PrintStream(args[1]);
		}
		    File target = new File("/tmp/stormdeployertmp/"+args[0]);
            if(!target.exists())
            	throw new Exception("The topology "+ args[0] + " never got deployed with the deployer");

           
	            
	            
	        // Uneploy the topology
            StormUndeployer su = new StormUndeployer();
	        su.undeploy("/opt/storm/latest/bin/storm kill","/tmp/stormdeployertmp/"+args[0], args[0],out);

            
		}
	

	

}
