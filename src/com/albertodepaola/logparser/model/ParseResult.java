package com.albertodepaola.logparser.model;

import java.util.List;
import java.util.Map;

public class ParseResult {

	private List<LogEntry> logEntries;
	private Map<String, Integer> ipCounter;
	private Map<String, BlockedIp> blockedIps;

	public ParseResult(List<LogEntry> logEntries, Map<String, Integer> ipCounter, Map<String, BlockedIp> blockedIps) {
		super();
		this.logEntries = logEntries;
		this.ipCounter = ipCounter;
		this.blockedIps = blockedIps;
	}
	
	public List<LogEntry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(List<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}

	public Map<String, Integer> getIpCounter() {
		return ipCounter;
	}

	public void setIpCounter(Map<String, Integer> ipCounter) {
		this.ipCounter = ipCounter;
	}

	public Map<String, BlockedIp> getBlockedIps() {
		return blockedIps;
	}

	public void setBlockedIps(Map<String, BlockedIp> blockedIps) {
		this.blockedIps = blockedIps;
	}
}
