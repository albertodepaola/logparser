package com.albertodepaola.logparser;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.albertodepaola.logparser.model.DURATION;
import com.albertodepaola.logparser.model.ParserResult;

public interface Parser<T>  {

	
	/**
	 * @return the file being parsed
	 */
	public File getAccessLog();
	
	/**
	 * @return the configured date to start counting from
	 */
	public Date getStartDate();
	
	public DURATION getDuration();
	
	public Integer getThreshold();
	
	/**
	 * @return List of objects extracted from parsed File
	 * @throws IOException
	 */
	public ParserResult<T> parse() throws IOException;
}
