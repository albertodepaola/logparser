package com.albertodepaola.logparser.model;

public class Blocked {

	private Long id;
	private String ip;
	private Integer ocurrences;
	private Integer appliedThreshold;
	private LogFile logFile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getOcurrences() {
		return ocurrences;
	}

	public void setOcurrences(Integer ocurrences) {
		this.ocurrences = ocurrences;
	}

	public Integer getAppliedThreshold() {
		return appliedThreshold;
	}

	public void setAppliedThreshold(Integer appliedThreshold) {
		this.appliedThreshold = appliedThreshold;
	}

	public LogFile getLogFile() {
		return logFile;
	}

	public void setLogFile(LogFile logFile) {
		this.logFile = logFile;
	}

}
