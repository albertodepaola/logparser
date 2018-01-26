package com.albertodepaola.logparser.model;

import java.util.Calendar;
import java.util.Date;

public enum DURATION {
	
	HOURLY("hourly", 1), DAILY("daily", 24);
	
	private String duration;
	private Integer sumHours;
		
	DURATION(String duration, Integer sumHours){
		this.duration = duration;
		this.sumHours = sumHours;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public Date getEndDate(Date startDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.HOUR_OF_DAY, this.sumHours);
		return calendar.getTime();
	}

}
