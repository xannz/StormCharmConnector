/**
 * 
 */
package com.ubuntu.stormcharmconnector;

/**
 * @author maarten
 *
 */
public class CassandraStormConnector extends AbstractStormCharmConnector {
	public final static String CASSANDRATYPE="cassandra";
	
	public CassandraStormConnector() {
		super(CASSANDRATYPE);
	}

}
