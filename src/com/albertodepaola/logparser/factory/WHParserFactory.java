package com.albertodepaola.logparser.factory;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.albertodepaola.logparser.Parser;
import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.DURATION;
import com.albertodepaola.logparser.model.ParserResult;
import com.albertodepaola.logparser.model.WHParserResult;

public class WHParserFactory implements AbstractParserFactory<WHParserResult> {

	@Override
	public Parser<WHParserResult> createParser(Map<String, String> argumentsMap) {
		return createParser(argumentsMap, Configuration.getConfiguration().getLogConfiguration());
	}

	public static Parser<WHParserResult> createParser(Map<String, String> argumentsMap, Map<String, Integer> configMap) {
		WHParser.ParserBuilder parserBuilder = null;
		// TODO use reflection to simply iterate over map to initialize parser
		try {
			if (argumentsMap.containsKey("accesslog")) {
				parserBuilder = new WHParser.ParserBuilder(argumentsMap.get("accesslog"));
			} else {
				throw new RuntimeException("No log file config in argument list. ");
			}
			if (argumentsMap.containsKey("startDate")) {
				try {
					parserBuilder.startDate(argumentsMap.get("startDate"));
				} catch (ParseException e1) {
					System.err.println("Error parsing date. The valid format is yyyy-MM-dd.HH:mm:ss. Default value new Date() used.");
					e1.printStackTrace();
					parserBuilder.startDate(new Date());
				}
			} else {
				// default value is now
				parserBuilder.startDate(new Date());
			}
			if (argumentsMap.containsKey("duration")) {
				parserBuilder.duration(argumentsMap.get("duration"));
			} else {
				// default value is hourly
				parserBuilder.duration(DURATION.HOURLY);
			}
			if (argumentsMap.containsKey("threshold")) {
				parserBuilder.threshold(argumentsMap.get("threshold"));
			} else {
				// default value is 200 for hourly and 500 for daily
				if(parserBuilder.getDuration().equals(DURATION.HOURLY)) {
					parserBuilder.threshold("200");
				} else {
					parserBuilder.threshold("500");
				}
			}
			parserBuilder.configMap(configMap);
		} catch (FileNotFoundException e1) {
			System.err.println("Log file not found, terminating.");
			throw new RuntimeException(e1);
		} 
	
//		Parser<ParserResult<WHParserResult>> parser = new WHParser();
		Parser<WHParserResult> parser = parserBuilder.build();
		return parser;
	}

}
