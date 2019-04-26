package com.example.SpringBootRestApplication.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InvalidInputFaultDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	protected List<String> errorMessage;
	
	public List<String> getErrorMessage(){
		if(errorMessage == null) {
			errorMessage = new ArrayList<String>();
		}
		return this.errorMessage;
	}
	
	public InvalidInputFaultDetails withErrorMessage(String...values) {
		if(values!=null) {
			for(String value: values) {
				getErrorMessage().add(value);
			}
		}
		return this;
	}
	
	public InvalidInputFaultDetails withErrorMessage(Collection<String> values) {
		if(values!=null) {
			getErrorMessage().addAll(values);
		}
		return this;
	}

}
