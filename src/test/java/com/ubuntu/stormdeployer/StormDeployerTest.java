/**
 * 
 */
package com.ubuntu.stormdeployer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
/**
 * @author maarten
 *
 */
public class StormDeployerTest extends TestCase {
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StormDeployerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StormDeployerTest.class );
    }

    /**
     * Test execute method
     * @throws Exception 
     */
    public void testExecute() throws Exception
    {
    	StormDeployer sd = new StormDeployer();
    	OutputStream out = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(out);
    	
    	sd.execute("echo test",ps);
    	String output = new String(out.toString());
    	//System.out.println(output);
    	assertEquals("test\n",output);
    	ps.close();
    	out.close();
    }
    
    /**
     * Test wget method
     * @throws Exception 
     */
    public void testWget() throws Exception
    {
    	StormDeployer sd = new StormDeployer();
    	File red = new File("/tmp/red.gif");
    	sd.wget(new URL("http://www.htmlgoodies.com/images/red.gif"),red);
    	assertTrue(red.exists());
    	red.delete();
    	
    }
    
    /**
     * Test git method
     * @throws Exception 
     */
    public void testGit() throws Exception
    {
    	StormDeployer sd = new StormDeployer();
    	OutputStream out = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(out);

    	File dir = new File("/tmp/gittest");
    	if(dir.exists())
    		dir.delete();
    	sd.git("https://github.com/mectors/StormCharmConnector.git",dir,ps, "");
    	assertTrue(dir.exists());
    	sd.execute("rm -rf "+dir, ps);
    	ps.close();
    	out.close();
    }
    
    public void testReadTopology() throws Exception
    {
    	StormDeployer sd = new StormDeployer();
    	String topology = "topology:\n"+
    	    	"- name: name1\n"+
    	    	"  jar: jar\n"+
    	    	"  topologyclass: class\n" +
    	    	"  packaging: mvn package\n"+
    	    	"  repository: https://test.com/test.storm\n"+
    	    	"  scriptbeforepackaging: script1\n"+
    	    	"  scriptbeforedeploying: script2\n"+
    	    	"- datasources:\n"+
    	    	"  - parameters:\n" +
    	    	"    - {name: name1, value: value1}\n"+
    	    	"    - {name: name2, value: value2}\n"+
    	    	"    type: type\n"+
    	    	"    script: script\n";
    	PrintWriter out = new PrintWriter("/tmp/test.txt");
    	out.print(topology);
    	out.flush();
    	List<Topology> topologies = sd.readTopologies("/tmp/test.txt");
    	assertEquals("class",topologies.get(0).getTopologyclass());
    	assertEquals("https://test.com/test.storm",topologies.get(0).getRepository());    	
    	out.close();
    	new File("/tmp/test.txt").delete();
    }
    
    /**
     * Test deploy method
     * @throws Exception 
     */
    public void testDeploy() throws Exception
    {
    	StormDeployer sd = new StormDeployer();
    	OutputStream out = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(out);
    	sd.deploy("echo /opt/storm/latest/bin/storm jar","test.jar", "com.abc.Test", "mytoplogy", ps); 
    	String output = new String(out.toString());
    	//System.out.println(output);
    	assertEquals("Deploying:echo /opt/storm/latest/bin/storm jar test.jar com.abc.Test mytoplogy\n/opt/storm/latest/bin/storm jar test.jar com.abc.Test mytoplogy\n",output);
    	ps.close();
    	out.close();
    }
    
    /**
     * Test applyDataSources method
     * @throws Exception 
     */
    public void testApplyDataSources() throws Exception
    {
    	StormDeployer sd = new StormDeployer();
    	OutputStream out = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(out);
    	DataSource ds = new DataSource();
    	@SuppressWarnings("restriction")
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    	String response = "#!/bin/bash\necho test $0 $1\n";
    	MyHandler handler = new MyHandler();
    	handler.addResponse("/test", response);
        server.createContext("/test", handler);
        server.setExecutor(null); // creates a default executor
        server.start();
    	ds.setScript("http://localhost:8000/test");
    	ds.setType("test");
    	List<Parameter> parameters = new ArrayList<Parameter>();
    	Parameter param = new Parameter();
    	param.setName("name");
    	param.setValue("value");
    	parameters.add(param);
    	ds.setParameters(parameters);
    	sd.applyDataSource(ds, new File("/tmp/"), ps);
    	sd.execute("rm -rf /tmp/datasources", ps);
    	String output = new String(out.toString());
    	assertEquals("test /tmp/datasources/test/script name=value\n",output);
    	ps.close();
    	out.close();
    	server.stop(0);
    
    }
    
    /**
     * Test deploy Topology method
     * @throws Exception 
     */
    public void testDeployTopology() throws Exception
    {

    	StormDeployer sd = new StormDeployer();
    	OutputStream out = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(out);
    	sd.execute("rm -rf /tmp/stormdeploytmp", ps);
    	DataSource ds = new DataSource();
    	Topology top = new Topology();
    	ds.setScript("http://localhost:8000/ds");
    	ds.setType("test");
    	List<Parameter> parameters = new ArrayList<Parameter>();
    	Parameter param = new Parameter();
    	param.setName("name");
    	param.setValue("value");
    	parameters.add(param);
    	ds.setParameters(parameters);
    	List<DataSource> datasources = new ArrayList<DataSource>();
    	datasources.add(ds);
    	top.setDatasources(datasources);
    	top.setJar("jar");
    	top.setName("test");
    	top.setPackaging("maven");
    	top.setRepository("https://github.com/mectors/StormCharmConnector.git");
    	top.setScriptbeforedeploying("http://localhost:8000/dp");
    	top.setScriptbeforepackaging("http://localhost:8000/pk");
    	top.setTopologyclass("class");
    	@SuppressWarnings("restriction")
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    	String responseDS = "#!/bin/bash\necho test $0 $1\n";
    	String responseDP = "#!/bin/bash\necho test $0 $1\n";
    	String responsePK = "#!/bin/bash\necho test $0 $1\n";
    	MyHandler handler = new MyHandler();
    	handler.addResponse("/ds", responseDS);
    	handler.addResponse("/dp", responseDP);
    	handler.addResponse("/pk", responsePK);
        server.createContext("/ds", handler);
        server.createContext("/dp", handler);
        server.createContext("/pk", handler);
        server.setExecutor(null); // creates a default executor
        server.start();


 
    	sd.deploy("echo /opt/storm/latest/bin/storm jar",top, ps, "");
    	
    	String output = new String(out.toString());
    	//System.out.println(output);
    	sd.execute("rm -rf /tmp/datasources", ps);
    	sd.execute("rm -rf /tmp/stormdeployertmp", ps);
    	assertTrue(output.endsWith("/opt/storm/latest/bin/storm jar /tmp/stormdeployertmp/test/target/jar class test\n"));
    	ps.close();
    	out.close();
    	server.stop(0);
    
    }
    
    /**
     * Test undeploy Topology method
     * @throws Exception 
     */
    public void testUndeployTopology() throws Exception
    {
    	OutputStream out = new ByteArrayOutputStream();
    	PrintStream ps = new PrintStream(out);
    	StormUndeployer su = new StormUndeployer();
    	File test = new File("/tmp/deletetest");
    	test.mkdir();
    	su.undeploy("echo undeployment","/tmp/deletetest", "topology", ps);
    	if(test.exists())
    		fail("Directory not delete");
    	String output = new String(out.toString());
    	assertEquals("undeployment topology\n",output);
    	ps.close();
    	out.close();
    }


	static class MyHandler implements HttpHandler {
		private static Map<String,String> responses;
		public MyHandler()
		{
			if(responses==null)
				responses=new HashMap<String,String>();
		}
		public void addResponse(String context,String response)
		{
			responses.put(context, response);
		}
    public void handle(HttpExchange t) throws IOException {
    	String path = t.getHttpContext().getPath();
        String response = responses.get(path);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
}
