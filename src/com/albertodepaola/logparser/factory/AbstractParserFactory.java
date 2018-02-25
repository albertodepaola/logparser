package com.albertodepaola.logparser.factory;

import java.util.Map;

import com.albertodepaola.logparser.Parser;

public interface AbstractParserFactory<T> {
	
//	public Parser<ParserResult<T>> createParser();

	public Parser<T> createParser(Map<String, String> argumentsMap);

}
