package com.albertodepaola.logparser.model;

public class ClientLogEntry extends LogEntry {

	private String cnpj;
	private String businessName;
	private String businessArea;

	public LOG_ENTRY_TYPE getType() {
		return LOG_ENTRY_TYPE.CLIENT;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

}
