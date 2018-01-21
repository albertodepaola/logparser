package com.ef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.albertodepaola.logparser.model.DURATION;

import static java.util.stream.Collectors.*;

public class Parser {

	private File accesslog;
	private Date startDate;
	private DURATION duration;
	private Integer threshold;

	private Parser(ParserBuilder builder) {
		this.accesslog = builder.accesslog;
		this.startDate = builder.startDate;
		this.duration = builder.duration;
		this.threshold = builder.threshold;
	}

	public static class ParserBuilder {
		private File accesslog;
		public Date startDate;
		public DURATION duration;
		public Integer threshold;

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

		public Parser build() {
			return new Parser(this);
		}

	}
	
	private void parse() throws IOException {
		
		Reader reader = null;
		LineNumberReader lnr = null;
		try {
			reader = new FileReader(this.accesslog);
			lnr = new LineNumberReader(reader);
			String line = null;
			while((line = lnr.readLine()) != null) {
				System.out.println(line);
				List<String> lineList = Arrays.asList(line.split("\\|"));
				
				Map<Integer, String> indexColumnMap = IntStream.range(0, lineList.size())
		         .boxed()
		         .collect(toMap(i -> i, lineList::get));
				
				// TODO get from json column config
				System.out.println(indexColumnMap.get(0));
				System.out.println(indexColumnMap.get(1));
				System.out.println(indexColumnMap.get(2));
				System.out.println(indexColumnMap.get(3));
				System.out.println(indexColumnMap.get(4));
				
				System.out.println(lineList.get(0));
				System.out.println(lineList.get(1));
				System.out.println(lineList.get(2));
				System.out.println(lineList.get(3));
				System.out.println(lineList.get(4));
				System.out.println(lineList.size());
				System.exit(0);
				
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
