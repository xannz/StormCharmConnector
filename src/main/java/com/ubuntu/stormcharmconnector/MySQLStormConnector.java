/**
 * 
 */
package com.ubuntu.stormcharmconnector;

/**
 * @author maarten
 *
 */
public class MySQLStormConnector extends AbstractStormCharmConnector {
	public final static String MYSQLTYPE="mysql";
	
	public MySQLStormConnector() {
		super(MYSQLTYPE);
	}

}
