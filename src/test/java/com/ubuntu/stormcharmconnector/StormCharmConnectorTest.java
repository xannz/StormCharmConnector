package com.ubuntu.stormcharmconnector;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class StormCharmConnectorTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StormCharmConnectorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StormCharmConnectorTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testApp() throws Exception
    {
    	
    	StormConnector sc = new StormConnector();
    	Type type = new Type();
    	type.setName("mysql");
    	Instance instance = new Instance();
    	instance.setHost("test");
    	instance.setPassword("pw");
    	instance.setPort(123);
    	instance.setUser("user");
    	List<Instance> instances = new ArrayList<Instance>();
    	instances.add(instance);
    	type.setInstances(instances);
    	List<Type> types = new ArrayList<Type>();
    	types.add(type);
    	sc.setTypes(types);
    	StormConnectorConstructor constructor = new StormConnectorConstructor( StormConnector.class );
		Loader loader = new Loader(constructor);
		Yaml yaml = new Yaml(loader);
		System.out.println(yaml.dump(sc));
    	
    	String test = 
    			"types:\n" +
    			"- name: mongo\n" +
    			"  instances:\n" +
    			"  - {host: testip, port: 123, user: test, password: ok}\n" +
    			"  - {host: testip2,port: 345}\n" +
    			"- name: mysql\n" + 
    			"  instances:\n" +
    			"  - {host: testipmysql, port: 123, user: test, password: ok}\n" +
    			"- name: cassandra\n" +
    			"  instances:\n" +
    			"  - {host: testipcassandra, port: 123, user: test, password: ok}\n";
        StormCharmConnector mysql = new MySQLStormConnector();
        StormCharmConnector cassandra = new CassandraStormConnector();
        StormCharmConnector mongo = new MongoStormConnector();
        List<Instance> conns = mysql.getConnections(new StringReader(test));
        assertEquals("testipmysql",conns.get(0).getHost());
        assertEquals(new Integer(123),conns.get(0).getPort());
        assertEquals("test",conns.get(0).getUser());
        assertEquals("ok",conns.get(0).getPassword());
        conns = cassandra.getConnections(new StringReader(test));
        assertEquals("testipcassandra",conns.get(0).getHost());
        assertEquals(new Integer(123),conns.get(0).getPort());
        assertEquals("test",conns.get(0).getUser());
        assertEquals("ok",conns.get(0).getPassword());
        conns = mongo.getConnections(new StringReader(test));
        assertEquals("testip",conns.get(0).getHost());
        assertEquals(new Integer(123),conns.get(0).getPort());
        assertEquals("test",conns.get(0).getUser());
        assertEquals("ok",conns.get(0).getPassword());
        assertEquals("testip2",conns.get(1).getHost());
        assertEquals(new Integer(345),conns.get(1).getPort());
        assertNull(conns.get(1).getUser());
        assertNull(conns.get(1).getPassword());
        
        
    }
}
