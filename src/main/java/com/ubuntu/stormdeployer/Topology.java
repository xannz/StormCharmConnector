/**
 * 
 */
package com.ubuntu.stormdeployer;

import java.util.List;

/**
 * @author maarten
 *
 */
public class Topology {
	public Topology()
	{
		
	}
	private String name;
	private String repository;
	private String topologyclass;
	private String jar;
	private String packaging;
	private String scriptbeforepackaging;
	private String scriptbeforedeploying;
	private List<DataSource> datasources;
        
        private String release;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTopologyclass() {
		return topologyclass;
	}
	public void setTopologyclass(String topologyclass) {
		this.topologyclass = topologyclass;
	}
	public String getJar() {
		return jar;
	}
	public void setJar(String jar) {
		this.jar = jar;
	}
	public String getPackaging() {
		return packaging;
	}
	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}
	public String getScriptbeforepackaging() {
		return scriptbeforepackaging;
	}
	public void setScriptbeforepackaging(String scriptbeforepackaging) {
		this.scriptbeforepackaging = scriptbeforepackaging;
	}
	public String getScriptbeforedeploying() {
		return scriptbeforedeploying;
	}
	public void setScriptbeforedeploying(String scriptbeforedeploying) {
		this.scriptbeforedeploying = scriptbeforedeploying;
	}
	public List<DataSource> getDatasources() {
		return datasources;
	}
	public void setDatasources(List<DataSource> datasources) {
		this.datasources = datasources;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
        
        
        public String getRelease() {
            return release;
        }
        public void setRelease(String release) {
            this.release = release;
        }
}
