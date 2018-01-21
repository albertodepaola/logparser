package com.albertodepaola.logparser.model;

import java.util.Date;

public class LogEntry {

	private Long id;
	private String ip;
	private Date date;
	private String request;
	private Integer status;
	private String userAgent;
	private String completeLine;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getCompleteLine() {
		return completeLine;
	}

	public void setCompleteLine(String completeLine) {
		this.completeLine = completeLine;
	}

	public LogFile getLogFile() {
		return logFile;
	}

	public void setLogFile(LogFile log) {
		this.logFile = log;
	}

}