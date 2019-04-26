package com.example.SpringBootRestApplication.exceptions;

import java.io.Serializable;


public class NoDataFoundFaultDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String errorMessage;
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public NoDataFoundFaultDetails withErrorMessage(String value) {
		setErrorMessage(value);
		return this;
	}

}
