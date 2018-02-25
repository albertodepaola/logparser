package com.albertodepaola.logparser.factory;

import java.util.List;
import java.util.Map;

import com.albertodepaola.logparser.model.ClientLogEntry;
import com.albertodepaola.logparser.model.Configuration;
import com.albertodepaola.logparser.model.LogEntry;
import com.albertodepaola.logparser.model.SalesLogEntry;
import com.albertodepaola.logparser.model.SalesmanLogEntry;

public class LogEntryFactory {
	
	public static LogEntry createEntry(List<String> line, Map<String, Integer> configMap) {
		
		LogEntry logEntry = null;
		String id = line.get(0);
		if(id != null && id.equals(Configuration.getConfiguration().getSalesmanType())) {
			SalesmanLogEntry sle = new SalesmanLogEntry();
			sle.setCpf(line.get(configMap.get("cpf")));
			sle.setName(line.get(configMap.get("name")));
			sle.setSalary(line.get(configMap.get("salary")));
			logEntry = sle;
		} else if(id.equals(Configuration.getConfiguration().getClientType())) {
			ClientLogEntry cle = new ClientLogEntry();
			cle.setBusinessArea(line.get(configMap.get("businessArea")));
			cle.setBusinessName(line.get(configMap.get("businessName")));
			cle.setCnpj(line.get(configMap.get("cnpj")));
			logEntry = cle;
		} else if(id.equals(Configuration.getConfiguration().getSalesType())) {
			SalesLogEntry sle = new SalesLogEntry();
			// TODO try for numberformat exception
			sle.setSalesId(Long.valueOf(line.get(configMap.get("salesId"))));
			sle.setSalesItemList(line.get(configMap.get("salesItemList")));
			sle.setSalesmanName(line.get(configMap.get("salesmanName")));
			logEntry = sle;
		} else {
			// TODO add ip log entry
			logEntry = new LogEntry();
		}
			
		
		return logEntry;
	}
	
	

}
