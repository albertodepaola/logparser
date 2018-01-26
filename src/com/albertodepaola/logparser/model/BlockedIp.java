package com.albertodepaola.logparser.model;

public class BlockedIp {

	private Long id;
	private String ip;
	private String ipv6;
	private Integer occurrences = 1;
	private String description;
	private LogFile logFile;

	
	public BlockedIp(LogEntry le) {
		this.ip = le.getIp();
		this.ipv6 = le.getIpv6();
		this.logFile = le.getLogFile();
	}

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

	public String getIpv6() {
		return ipv6;
	}

	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	public Integer getOccurrences() {
		return occurrences;
	}

	public void setOccurrences(Integer occurrences) {
		this.occurrences = occurrences;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LogFile getLogFile() {
		return logFile;
	}

	public void setLogFile(LogFile log) {
		this.logFile = log;
	}
	
	@Override
	public String toString(){
		// TODO use bean utils
		return this.getIp() + " made " + this.getOccurrences() + " requests";
	}

}
