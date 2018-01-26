package com.ef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.albertodepaola.logparser.db.BlockedIpDAOImpl;
import com.albertodepaola.logparser.db.LogEntryDAOImpl;
import com.albertodepaola.logparser.db.LogFileDAOImpl;
import com.albertodepaola.logparser.model.BlockedIp;
import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.DURATION;
import com.albertodepaola.logparser.model.LogEntry;
import com.albertodepaola.logparser.model.LogFile;
import com.albertodepaola.logparser.model.ParseResult;

public class Parser {

	private File accesslog;
	private Date startDate;
	private DURATION duration;
	private Integer threshold;
	private Map<String, Integer> configMap;
	private static Configuration configuration;

	private Parser(ParserBuilder builder) {
		this.accesslog = builder.accesslog;
		this.startDate = builder.startDate;
		this.duration = builder.duration;
		this.threshold = builder.threshold;
		this.configMap = builder.configMap;
	}

	public static class ParserBuilder {
		private File accesslog;
		private Date startDate;
		private DURATION duration;
		private Integer threshold;
		private Map<String, Integer> configMap;
		

		public ParserBuilder(File file) throws FileNotFoundException {
			if (file.exists())
				this.accesslog = file;
			else
				throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist");
		}

		public ParserBuilder(String filePath) throws FileNotFoundException {
			this(new File(filePath));
		}

		public ParserBuilder startDate(String startDate) throws ParseException {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
			return this.startDate(sdf.parse(startDate));
		}

		public ParserBuilder startDate(Date startDate) {
			this.startDate = startDate;
			return this;
		}

		public ParserBuilder duration(String duration) {
			return this.duration(DURATION.valueOf(duration.toUpperCase()));
		}
		
		public ParserBuilder duration(DURATION duration) {
			this.duration = duration;
			return this;
		}

		public ParserBuilder threshold(String threshold) {
			this.threshold = Integer.valueOf(threshold);
			return this;
		}
		
		public void configMap(Map<String, Integer> configMap) {
			this.configMap = configMap;
		}
		
		public Parser build() {
			return new Parser(this);
		}

	}
	
	private ParseResult parse() throws IOException {
		
		List<LogEntry> logEntries = new ArrayList<>();
		Map<String, Integer> ipCounter = new HashMap<>();
		Map<String, BlockedIp> blockedIpsMap = new HashMap<>();
		
		
		try (Reader reader = new FileReader(this.accesslog);
				LineNumberReader lnr = new LineNumberReader(reader);){
			
			SimpleDateFormat sdf = Configuration.getConfiguration().getFormater();
			Date endDate = duration.getEndDate(startDate);

			// read file to the end with buffered line reader
			String line = null;
			while((line = lnr.readLine()) != null) {
				
				List<String> lineList = Arrays.asList(line.split("\\|"));
				
				Map<Integer, String> indexColumnMap = new HashMap<>();
				int count = 0;
				for (String string : lineList) {
					indexColumnMap.put(count++, string);
				}
				
				LogEntry le = new LogEntry();
				
				// reads from log file to object based on index defined in config file.
				// TODO for full configurability the object should be as defined in config file
				le.setIp(indexColumnMap.get(configMap.get("ipv4")));
				le.setRequest(indexColumnMap.get(configMap.get("request")));
				le.setUserAgent(indexColumnMap.get(configMap.get("userAgent")));
				try {
					le.setDate(sdf.parse(indexColumnMap.get(configMap.get("date"))));
				} catch (ParseException pe) { System.err.println("Error parsing date in this log line: " + line); pe.printStackTrace();}
				try {
					le.setStatus(Integer.parseInt(indexColumnMap.get(configMap.get("status"))));
				} catch (NumberFormatException nfe) { System.err.println("Error parsing status number in this log line: " + line); nfe.printStackTrace(); }
				
				// saves the whole line for posible future use
				le.setCompleteLine(line);
				logEntries.add(le);
				
				// only this ips should go to the second table.
				if(le.getDate().after(startDate) && le.getDate().before(endDate)) {
					if(!ipCounter.containsKey(le.getIp())) {
						ipCounter.put(le.getIp(), 1);
					} else {
						int occurrences = ipCounter.get(le.getIp()) + 1;
						ipCounter.put(le.getIp(), occurrences);
						
						if(occurrences > this.threshold) {
							if(!blockedIpsMap.containsKey(le.getIp())) {
								// if not on map, creates the instance
								blockedIpsMap.put(le.getIp(), new BlockedIp(le));
							} else {
								// if already on map, updates it occurrence count 
								blockedIpsMap.get(le.getIp()).setOccurrences(occurrences);
							}
						}
					}
				}
				
			}
			
			for (BlockedIp blockedIp : blockedIpsMap.values()) {
				String blockedIpDescription = blockedIp.toString() + " between " + sdf.format(startDate) + " and " + sdf.format(endDate);
				blockedIp.setDescription(blockedIpDescription);
				System.out.println(blockedIp.getDescription());
			}
			
			return new ParseResult(logEntries, ipCounter, blockedIpsMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
	}

	private static Parser createParser(Map<String, String> argumentsMap, Map<String, Integer> configMap) {
		ParserBuilder parserBuilder = null;
		// TODO use reflection to simply iterate over map to initialize parser
		try {
			if (argumentsMap.containsKey("accesslog")) {
				parserBuilder = new Parser.ParserBuilder(argumentsMap.get("accesslog"));
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
				if(parserBuilder.duration.equals(DURATION.HOURLY)) {
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

		Parser parser = parserBuilder.build();
		return parser;
	}
	
	public File getAccesslog() {
		return accesslog;
	}

	public Date getStartDate() {
		return startDate;
	}

	public DURATION getDuration() {
		return duration;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public Map<String, Integer> getConfigMap() {
		return configMap;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

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
			
			Parser parser = createParser(argumentsMap, Configuration.getConfiguration().getLogConfiguration());
			
			try {
				
				LogFile lf = null;
				if(Configuration.getConfiguration().getSaveLogToDatabase()) {
					lf = new LogFile(parser.getAccesslog(), parser.getStartDate(), parser.getDuration(), parser.getThreshold(), new Date());
					LogFileDAOImpl logFileDAO = new LogFileDAOImpl();
					lf = logFileDAO.insert(lf);
				}
				
				ParseResult parseResult = parser.parse();
				
				if(Configuration.getConfiguration().getSaveLogToDatabase()) {
					// TODO change to IoC container
					// inserting in database
					LogEntryDAOImpl logEntryDAO = new LogEntryDAOImpl();
					logEntryDAO.insertBatch(parseResult.getLogEntries(), lf);
					BlockedIpDAOImpl blockedIpDAO = new BlockedIpDAOImpl();
					blockedIpDAO.insertBatch(parseResult.getBlockedIps().values(), lf);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				// TODO log error
			} catch (SQLException e1) {
				e1.printStackTrace();
				// TODO log error
			}
		} catch (Exception e) {
			System.err.println("Error while executing Log Parser");
			e.printStackTrace();
		}
		
	}

}
