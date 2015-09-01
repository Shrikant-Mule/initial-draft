package com.copart.figs;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="app", ignoreNestedProperties=false)
@Component
public class ApplicationConfiguration {
	private static Logger logger = Logger.getLogger(ApplicationConfiguration.class);
	private String name;
	private String figsUrl;
	private String update;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFigsUrl() {
		return figsUrl;
	}

	public void setFigsUrl(String figsUrl) {
		this.figsUrl = figsUrl;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}
	
	

}
