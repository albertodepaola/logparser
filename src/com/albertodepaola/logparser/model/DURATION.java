package com.albertodepaola.logparser.model;

public enum DURATION {
	
	HOURLY("hourly"), DAILY("daily");
	
	private String duration;
		
	DURATION(String duration){
		this.duration = duration;
	}
	
	public String getDuration() {
		return duration;
	}

}
