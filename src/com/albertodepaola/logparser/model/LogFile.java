package com.albertodepaola.logparser.model;

import java.io.File;
import java.util.Date;

public class LogFile {

	private Long id;
	private File accessLog;
	private Date startDate;
	private DURATION duration;
	private Integer threshold;
	private Date processDate;
	private String md5;

	public LogFile() {

	}

	public LogFile(File accessLog, Date startDate, DURATION duration, Integer threshold, Date processDate) {
		super();
		this.accessLog = accessLog;
		this.startDate = startDate;
		this.duration = duration;
		this.threshold = threshold;
		this.processDate = processDate;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public File getAccessLog() {
		return accessLog;
	}

	public void setAccessLog(File accessLog) {
		this.accessLog = accessLog;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public DURATION getDuration() {
		return duration;
	}

	public void setDuration(DURATION duration) {
		this.duration = duration;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
