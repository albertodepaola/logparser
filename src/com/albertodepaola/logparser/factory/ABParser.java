package com.albertodepaola.logparser.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.albertodepaola.logparser.Parser;
import com.albertodepaola.logparser.model.ABParserResult;
import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.DURATION;
import com.albertodepaola.logparser.model.LOG_ENTRY_TYPE;
import com.albertodepaola.logparser.model.LogEntry;
import com.albertodepaola.logparser.model.ParserResult;
import com.albertodepaola.logparser.model.Result;
import com.albertodepaola.logparser.model.SalesLogEntry;

public class ABParser<T> implements Parser<ABParserResult> {
	
	private File accesslog;
	private Date startDate;
	private DURATION duration;
	private Integer threshold;
	private Map<String, Integer> configMap;
	private static Configuration configuration;

	private ABParser(ParserBuilder builder) {
		this.accesslog = builder.accesslog;
		this.startDate = builder.startDate;
		this.duration = builder.duration;
		this.threshold = builder.threshold;
		this.configMap = builder.configMap;
	}

	public ABParser() {
		// TODO Auto-generated constructor stub
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
		
		public ABParser build() {
			return new ABParser(this);
		}
		
		public DURATION getDuration() {
			return this.duration;
		}

	}

	public ParserResult<ABParserResult> parse() throws IOException {
		
		try (Reader reader = new FileReader(this.accesslog);
				LineNumberReader lnr = new LineNumberReader(reader);){
			
			Long quantidadeDeClientes = 0L;
			Long quantidadeDeVendedores = 0L;
			Long idMaiorVenda = 0L;
			String worstSeller = "";
			BigDecimal maiorVenda = BigDecimal.ZERO;
			BigDecimal menorVenda = BigDecimal.ZERO;
			
			// read file to the end with buffered line reader
			String line = null;
			while((line = lnr.readLine()) != null) {
								
				List<String> lineList = Arrays.asList(line.split(Configuration.getConfiguration().getLineSeparator()));
				
				Map<Integer, String> indexColumnMap = new HashMap<>();
				int count = 0;
				for (String string : lineList) {
					indexColumnMap.put(count++, string);
				}
				
				LogEntry le = LogEntryFactory.createEntry(lineList, configMap);
				if(LOG_ENTRY_TYPE.SALESMAN.equals(le.getType())) {
					quantidadeDeVendedores++;
					
				} else if(LOG_ENTRY_TYPE.CLIENT.equals(le.getType())) {
					quantidadeDeClientes++;
				} else if(LOG_ENTRY_TYPE.SALES.equals(le.getType())) {
					SalesLogEntry sle = (SalesLogEntry)le;
					if(maiorVenda.compareTo(sle.getSaleAmount()) < 0) {
						maiorVenda = sle.getSaleAmount();
						idMaiorVenda = sle.getSalesId();
					}
					if(menorVenda.compareTo(BigDecimal.ZERO) == 0 || menorVenda.compareTo(sle.getSaleAmount()) > 0) {
						menorVenda = sle.getSaleAmount();
						worstSeller = sle.getSalesmanName();
					} 
				}
				
			}
			
			return new Result<ABParserResult>(new ABParserResult(quantidadeDeClientes, quantidadeDeVendedores, idMaiorVenda, worstSeller));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
	}

	public File getAccessLog() {
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

	

}
