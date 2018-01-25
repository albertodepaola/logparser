package com.ef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.DURATION;
import com.albertodepaola.logparser.model.LogEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Parser {

	private File accesslog;
	private Date startDate;
	private DURATION duration;
	private Integer threshold;
	private Map<String, Integer> configMap;

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
		
		public ParserBuilder filePath(String configFilePath) throws FileNotFoundException {
			File f = new File(configFilePath);
			if (f.exists()) {
				Gson g = new Gson();
				FileReader fr = new FileReader(f);

				Type type = new TypeToken<HashMap<String, Integer>>() {}.getType();

//				HashMap<String, Integer> configMap = g.fromJson(fr, type);
				Configuration c = g.fromJson(fr, Configuration.class);
				this.configMap = c.getConfiguration();
			} else
				throw new FileNotFoundException("The file " + f.getAbsolutePath() + " does not exist");
			
			return this;
		}

		public Parser build() {
			return new Parser(this);
		}



	}
	
	private void parse() throws IOException {
		
		Reader reader = null;
		LineNumberReader lnr = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			reader = new FileReader(this.accesslog);
			lnr = new LineNumberReader(reader);
			String line = null;
			Map<String, Integer> ipCounter = new HashMap<>();
			long startTime = System.currentTimeMillis();
//			long startTimeNano = System.nanoTime();
			while((line = lnr.readLine()) != null) {
				
//				System.out.println(startTime);
				List<String> lineList = Arrays.asList(line.split("\\|"));
//				System.out.println("Array Split time: " + (System.currentTimeMillis() - startTime));
//				System.out.println("Array Split time: " + (System.nanoTime() - startTimeNano));
				
//				startTime = System.currentTimeMillis();
//				startTimeNano = System.nanoTime();
				// not used because of performance issues
				/*
				Map<Integer, String> indexColumnMap = IntStream.range(0, lineList.size())
		         .boxed()
		         .collect(toMap(i -> i, lineList::get));
				*/
//				System.out.println("Collect to Map time: " + (System.currentTimeMillis() - startTime));
//				System.out.println("Collect to Map time: " + (System.nanoTime() - startTimeNano));
				
//				startTime = System.currentTimeMillis();
//				startTimeNano = System.nanoTime();
				Map<Integer, String> indexColumnMap = new HashMap<>();
				int count = 0;
				for (String string : lineList) {
					indexColumnMap.put(count++, string);
				}
//				System.out.println("Collect to Map time: " + (System.currentTimeMillis() - startTime));
//				System.out.println("Collect to Map time: " + (System.nanoTime() - startTimeNano));
				
				// TODO get from json column config
				LogEntry le = new LogEntry();
				
				le.setIp(indexColumnMap.get(configMap.get("ipv4")));
				le.setRequest(indexColumnMap.get(configMap.get("request")));
				le.setUserAgent(indexColumnMap.get(configMap.get("userAgent")));
				try {
					le.setDate(sdf.parse(indexColumnMap.get(configMap.get("date"))));
				} catch (ParseException pe) { System.err.println("Error parsing date in this log line: " + line); pe.printStackTrace();}
				try {
					le.setStatus(Integer.parseInt(indexColumnMap.get(configMap.get("status"))));
				} catch (NumberFormatException nfe) { System.err.println("Error parsing status number in this log line: " + line); nfe.printStackTrace(); }
				
				le.setCompleteLine(line);
				
				if(!ipCounter.containsKey(le.getIp())) {
					ipCounter.put(le.getIp(), 1);
				} else {
					ipCounter.put(le.getIp(), ipCounter.get(le.getIp()) + 1);
				}
				
//				System.out.println(le);
				
//				System.exit(0);
				
			}
			System.out.println("actual parsing " + (System.currentTimeMillis() - startTime));
			
			for (Entry<String, Integer> entry : ipCounter.entrySet()) {
				Integer value = entry.getValue();
				if(value > this.threshold) {
					System.out.println(entry.getKey());
					System.out.println(value);
				}
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) { } // silence close exception
			}
			if(lnr != null) {
				try {
					lnr.close();
				} catch (IOException ioe) { } // silence close exception
			}
			
		}
		
		
	}

	private static Parser createParser(Map<String, String> argumentsMap) {
		ParserBuilder parserBuilder = null;
		// TODO use reflection to simply iterate over map to initialize parser
		try {
			if (argumentsMap.containsKey("accesslog")) {
				parserBuilder = new Parser.ParserBuilder(argumentsMap.get("accesslog"));
			} else {
				// TODO mandatory argument, throw exception?
			}
			if (argumentsMap.containsKey("startDate")) {
				parserBuilder.startDate(argumentsMap.get("startDate"));
			} else {
				// default value is now
				parserBuilder.startDate(new Date());
			}
			if (argumentsMap.containsKey("duration")) {
				parserBuilder.duration(argumentsMap.get("duration"));
			} else {
				// default value is hourly, to work less
				parserBuilder.duration(DURATION.HOURLY);
			}
			if (argumentsMap.containsKey("threshold")) {
				parserBuilder.threshold(argumentsMap.get("threshold"));
			} else {
				// default value is 100
				parserBuilder.threshold("100");
			}
			if(argumentsMap.containsKey("jsonConfig")) {
				parserBuilder.filePath(argumentsMap.get("jsonConfig"));
			} else {
				parserBuilder.filePath("src/com/ef/config.json");
			}
			// TODO handle errors
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			System.err.println("Error parsing date. The valid format is yyyy-MM-dd.HH:mm:ss");
			e1.printStackTrace();
		}

		Parser parser = parserBuilder.build();
		return parser;
	}
	
	public static void main(String[] args) {
		
		Map<String, String> argumentsMap = 
				Arrays.asList(args)
				.stream()
				.map(elem -> elem.split("="))
				.collect(Collectors.toMap(e -> e[0].replace("--", ""), e -> e[1]));

		// TODO validate help and optional parameters
		
		Parser parser = createParser(argumentsMap);
		
		try {
			parser.parse();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

}
