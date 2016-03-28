/**
 * 
 */
package com.ubuntu.stormcharmconnector;

/**
 * @author maarten
 *
 */
public class MongoStormConnector extends AbstractStormCharmConnector {
	public final static String MONGOTYPE="mongo";
	
	public MongoStormConnector() {
		super(MONGOTYPE);
	}

}
