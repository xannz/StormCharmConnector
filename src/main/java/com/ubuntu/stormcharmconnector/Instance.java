/**
 * 
 */
package com.ubuntu.stormcharmconnector;

/**
 * @author maarten
 *
 */
public class Instance {
	public Instance()
	{
		
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}

	private String host;
	private Integer port;
	private String user;
	private String password;
}
