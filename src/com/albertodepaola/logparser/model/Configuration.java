package com.albertodepaola.logparser.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.google.gson.Gson;

public class Configuration {

	private static Configuration configuration;
	private String documentation;
	private Map<String, Integer> logConfiguration;
	private Boolean saveLogToDatabase;
	private Map<String, String> databaseConfiguration;
	private String simpleDateFormat;
	private static SimpleDateFormat sdf;
	private Integer batchSize;
	private String lineSeparator;
	private String parserType;
	private String salesmanType;
	private String clientType;
	private String salesType;

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public Map<String, Integer> getLogConfiguration() {
		return logConfiguration;
	}

	public void setLogConfiguration(Map<String, Integer> configuration) {
		this.logConfiguration = configuration;
	}

	public Boolean getSaveLogToDatabase() {
		return saveLogToDatabase;
	}

	public void setSaveLogToDatabase(Boolean saveLogToDatabase) {
		this.saveLogToDatabase = saveLogToDatabase;
	}

	public Map<String, String> getDatabaseConfiguration() {
		return databaseConfiguration;
	}

	public void setDatabaseConfiguration(Map<String, String> databaseConfiguration) {
		this.databaseConfiguration = databaseConfiguration;
	}
	
	public static Configuration getConfiguration() {
		return configuration;
	}
	
	public String getSimpleDateFormat() {
		return simpleDateFormat;
	}

	public void setSimpleDateFormat(String simpleDateFormat) {
		this.simpleDateFormat = simpleDateFormat;
	}
	
	public SimpleDateFormat getFormater() {
		return sdf;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}
	
	public String getLineSeparator() {
		return this.lineSeparator;
	}
	
	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public String getParserType() {
		return parserType;
	}

	public void setParserType(String parserType) {
		this.parserType = parserType;
	}

	public String getSalesmanType() {
		return salesmanType;
	}

	public void setSalesmanType(String salesmanType) {
		this.salesmanType = salesmanType;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public static void loadConfigurationFromJsonFile(String jsonFilePath) throws FileNotFoundException {
		File f = new File(jsonFilePath);
		if (f.exists()) {
			Gson g = new Gson();
			FileReader fr = new FileReader(f);

			configuration = g.fromJson(fr, Configuration.class);
			sdf = new SimpleDateFormat(configuration.getSimpleDateFormat());
			
		} else
			throw new FileNotFoundException("The file " + f.getAbsolutePath() + " does not exist");
	}

	

}
