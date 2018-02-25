package com.albertodepaola.logparser.model;

public class Result<T> implements ParserResult<T> {

	private T result; 
	
	public Result(T result) {
		this.result = result;
	}
	
	@Override
	public T getResult() {
		return this.result;
	}
	

}
