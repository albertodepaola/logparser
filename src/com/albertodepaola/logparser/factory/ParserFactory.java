package com.albertodepaola.logparser.factory;

import java.util.Map;

import com.albertodepaola.logparser.Parser;
import com.albertodepaola.logparser.model.ParserResult;

public class ParserFactory<T> {
	
	public Parser<T> createParser(AbstractParserFactory<T> factory, Map<String, String> argumentsMap) {
		return factory.createParser(argumentsMap);
	}

}
