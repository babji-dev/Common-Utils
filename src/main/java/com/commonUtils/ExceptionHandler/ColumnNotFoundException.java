package com.commonUtils.ExceptionHandler;

public class ColumnNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
//HELLO
	public ColumnNotFoundException(String columnName) {
		super("No Column Found with columnName "+columnName);
	}

}
