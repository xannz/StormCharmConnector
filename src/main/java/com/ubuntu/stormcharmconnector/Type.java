/**
 * 
 */
package com.ubuntu.stormcharmconnector;

import java.util.List;

/**
 * @author maarten
 *
 */
public class Type {
	private String name;
	private List<Instance> instances;
	public Type()
	{
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Instance> getInstances() {
		return instances;
	}
	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}
	
}
