/**
 * 
 */
package com.ubuntu.stormdeployer;

import java.util.List;

/**
 * @author maarten
 *
 */
public class DataSource {
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	private String type;
	private String script;
	private List<Parameter> parameters;
}
