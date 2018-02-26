package com.albertodepaola.logparser.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import com.albertodepaola.logparser.Parser;
import com.albertodepaola.logparser.db.BlockedIpDAOImpl;
import com.albertodepaola.logparser.db.LogEntryDAOImpl;
import com.albertodepaola.logparser.db.LogFileDAOImpl;
import com.albertodepaola.logparser.factory.ABParserFactory;
import com.albertodepaola.logparser.factory.ParserFactory;
import com.albertodepaola.logparser.factory.WHParserFactory;
import com.albertodepaola.logparser.model.ABParserResult;
import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.LogFile;
import com.albertodepaola.logparser.model.ParserResult;
import com.albertodepaola.logparser.model.WHParserResult;

public class ParserExample {

	public static void main(String[] args) {
		
		try {
			Map<String, String> argumentsMap = 
					Arrays.asList(args)
					.stream()
					.map(elem -> elem.split("="))
					.collect(Collectors.toMap(e -> e[0].replace("--", ""), e -> e[1]));

			// TODO validate help and optional parameters
			
			String configFilePath;
			
			if(argumentsMap.containsKey("jsonConfig")) {
				configFilePath = argumentsMap.get("jsonConfig");
			} else {
				configFilePath = "src/com/ef/config.json";
			}
			
			Configuration.loadConfigurationFromJsonFile(configFilePath);
			
			if(argumentsMap.get("parserType").equals("AB")) {
				Parser<ABParserResult> abparser = new ParserFactory<ABParserResult>().createParser(new ABParserFactory(), argumentsMap);
				ParserResult<ABParserResult> parse = abparser.parse();
				
				System.out.println(parse.getResult().getQuantidadeDeVendedores());
				System.out.println(parse.getResult().getQuantidadeDeClientes());
				System.out.println(parse.getResult().getIdMaiorVenda());
				System.out.println(parse.getResult().getWorstSeller());
				
			} else {
				Parser<WHParserResult> whparser = new ParserFactory<WHParserResult>().createParser(new WHParserFactory(), argumentsMap);
				try {
					
					LogFile lf = null;
					if(Configuration.getConfiguration().getSaveLogToDatabase()) {
						lf = new LogFile(whparser.getAccessLog(), whparser.getStartDate(), whparser.getDuration(), whparser.getThreshold(), new Date());
						LogFileDAOImpl logFileDAO = new LogFileDAOImpl();
						lf = logFileDAO.insert(lf);
					}
					
					ParserResult<WHParserResult> parseResult = whparser.parse();
					
					if(Configuration.getConfiguration().getSaveLogToDatabase()) {
						// TODO change to IoC containerj
						// inserting in database
						LogEntryDAOImpl logEntryDAO = new LogEntryDAOImpl();
						logEntryDAO.insertBatch(parseResult.getResult().getLogEntries(), lf);
						BlockedIpDAOImpl blockedIpDAO = new BlockedIpDAOImpl();
						blockedIpDAO.insertBatch(parseResult.getResult().getBlockedIps().values(), lf);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			
			
		} catch (Exception e) {
			System.err.println("Error while executing Log Parser");
			e.printStackTrace();
		}
	}

}
