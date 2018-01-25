package com.albertodepaola.logparser.model;

import java.util.Map;

public class Configuration {

	private String documentation;
	private Map<String, Integer> configuration;

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public Map<String, Integer> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, Integer> configuration) {
		this.configuration = configuration;
	}

}
