/**
 * 
 */
package com.ubuntu.stormcharmconnector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

import com.ubuntu.stormdeployer.Topologies;
import com.ubuntu.stormdeployer.TopologiesConstructor;

/**
 * @author maarten
 * This class reads a yaml file called connections.yaml that is located in /opt/storm/latest/conf.
 * The subordinate StormDeployer charm will update this file with the appropriate connections.
 * Expected format for connections.yaml
 * type: postgress\n
 *    - {host: testip,port: 123,user: test,password: ok}\n
 *    - {host: testip2,port: 345}\n
 * type: mysql\n
 *    - {host: testip,port: 123,user: test,password: ok}\n
 *    - {host: testip2,port: 345}\n
 */
public abstract class AbstractStormCharmConnector implements
		StormCharmConnector {
	private String type;
	private static final String CONNECTIONS = "/opt/storm/latest/conf/connections.yaml";
	private long lastmodified;
	private List<Instance> connections;
	
	public AbstractStormCharmConnector(String type)
	{
		this.type=type;
	}
	
	/* (non-Javadoc)
	 * @see com.ubuntu.stormcharmconnector.StormCharmConnector#getConnections()
	 */
	public List<Instance> getConnections() throws NoConnectionsException {
		FileReader fr=null;
		try {
			File file = new File(CONNECTIONS);
			if(connections != null && file.lastModified() == lastmodified)
			{
				return connections;
			}
			fr = new FileReader(file);
			connections = getConnections(fr);
			lastmodified=file.lastModified();
			return connections;
		} catch (FileNotFoundException e) {
			throw new NoConnectionsException("Connections file not found or not readable",e);
		} finally
		{
			try {
				if(fr !=null)
					fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<Instance> getConnections(Reader connectionsfile) throws NoConnectionsException
	{


		StormConnectorConstructor constructor = new StormConnectorConstructor( StormConnector.class );
		Loader loader = new Loader(constructor);
		Yaml yaml = new Yaml(loader);
		StormConnector st = null;
		try
		{
			st = (StormConnector) yaml.load(connectionsfile);
			
		}catch(Exception e)
		{
			throw new NoConnectionsException("Connections file not found or not readable",e);
		}
		return st.getInstances(type);
	}

}
