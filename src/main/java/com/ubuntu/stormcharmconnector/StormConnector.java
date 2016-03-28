/**
 * 
 */
package com.ubuntu.stormcharmconnector;

import java.util.List;

/**
 * @author maarten
 * type:\n
 * 	 name: postgress\n
 *    - {host: testip,port: 123,user: test,password: ok}\n
 *    - {host: testip2,port: 345}\n
 * type: mysql\n
 *    - {host: testip,port: 123,user: test,password: ok}\n
 *    - {host: testip2,port: 345}\n
 */
public class StormConnector {
	public StormConnector()
	{
		
	}
	private List<Type> types;

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public List<Instance> getInstances(String typeName)
	{
		if(types==null)
			return null;
		for(Type type:getTypes())
		{
			if(type.getName().equals(typeName))
			{
				return type.getInstances();
			}
		}
		return null;
	}

}
