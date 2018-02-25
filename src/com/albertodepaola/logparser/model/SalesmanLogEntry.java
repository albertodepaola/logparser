package com.albertodepaola.logparser.model;

public class SalesmanLogEntry extends LogEntry {

	private String cpf;
	private String name;
	private String salary;

	public LOG_ENTRY_TYPE getType() {
		return LOG_ENTRY_TYPE.SALESMAN;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

}
