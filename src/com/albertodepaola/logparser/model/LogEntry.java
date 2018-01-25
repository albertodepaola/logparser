package com.albertodepaola.logparser.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {

	private Long id;
	private String ip;
	private String ipv6;
	private Date date;
	private String request;
	private Integer status;
	private String userAgent;
	private String completeLine;
	private LogFile logFile;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

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
	
	@Override
	public String toString(){
		// TODO use bean utils
		return this.ip + " - " + sdf.format(this.date) + " : " + this.completeLine;
	}

}
