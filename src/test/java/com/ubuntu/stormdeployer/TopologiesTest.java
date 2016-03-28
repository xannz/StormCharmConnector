/**
 * 
 */
package com.ubuntu.stormdeployer;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author maarten
 *
 */
public class TopologiesTest extends TestCase {
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TopologiesTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TopologiesTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testTopologies() throws Exception
    {
    	Topologies topologies = new Topologies();
    	Topology topology1 = new Topology();
    	Parameter param1 = new Parameter();
    	param1.setName("name1");
    	param1.setValue("value1");
    	Parameter param2 = new Parameter();
    	param2.setName("name2");
    	param2.setValue("value2");
    	List<Parameter> parameters = new ArrayList<Parameter>();
    	parameters.add(param1);
    	parameters.add(param2);
    	DataSource datasource1 = new DataSource();
    	datasource1.setParameters(parameters);
    	datasource1.setScript("script");
    	datasource1.setType("type");
    	DataSource datasource2 = new DataSource();
    	datasource2.setParameters(parameters);
    	datasource2.setScript("script2");
    	datasource2.setType("type2");
    	List<DataSource> datasources = new ArrayList<DataSource>();
    	datasources.add(datasource1);
    	datasources.add(datasource2);
    	topology1.setName("name1");
    	topology1.setDatasources(datasources);
    	topology1.setJar("jar");
    	topology1.setPackaging("mvn package");
    	topology1.setRepository("repo");
    	topology1.setScriptbeforepackaging("script1");
    	topology1.setScriptbeforedeploying("script2");
    	topology1.setTopologyclass("class");
    	Topology topology2 = new Topology();
    	topology2.setName("name2");
    	topology2.setDatasources(datasources);
    	topology2.setJar("jar2");
    	topology2.setPackaging("mvn package2");
    	topology2.setRepository("repo2");
    	topology2.setScriptbeforepackaging("script12");
    	topology2.setScriptbeforedeploying("script22");
    	topology2.setTopologyclass("class2");
    	List<Topology> tops = new ArrayList<Topology>();
    	tops.add(topology1);
    	tops.add(topology2);
    	topologies.setTopology(tops);
    	TopologiesConstructor constructor = new TopologiesConstructor( Topologies.class );

    	

    	Loader loader = new Loader(constructor);


    	Yaml yaml = new Yaml(loader);
    	String test = 
    	"topology:\n"+
    	"- name: name1\n"+
    	"  jar: jar\n"+
    	"  topologyclass: class\n" +
    	"  packaging: mvn package\n"+
    	"  repository: repo\n"+
    	"  scriptbeforepackaging: script1\n"+
    	"  scriptbeforedeploying: script2\n"+
    	"- datasources:\n"+
    	"  - parameters:\n" +
    	"    - {name: name1, value: value1}\n"+
    	"    - {name: name2, value: value2}\n"+
    	"    type: type\n"+
    	"    script: script\n";
    	String obj = yaml.dump(topologies);

    	assertEquals(Topologies.class,yaml.load(obj).getClass());
    	assertEquals(Topologies.class,yaml.load(test).getClass());
    	
    	
    }
}
