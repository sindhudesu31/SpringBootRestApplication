package com.example.SpringBootRestApplication.exceptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IssueNotFoundException extends Exception{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(IssueNotFoundException.class);
	private static final String ISSUE_NOT_FOUND = "Issue not found in system";
	private ResponseExcpetionMessage responseMsg= null;
	
	public IssueNotFoundException() {
		this(ISSUE_NOT_FOUND);
	}
	
	public IssueNotFoundException(String errorMessage) {
		super(errorMessage);
		responseMsg = new ResponseExcpetionMessage();
		responseMsg.setTitle(ISSUE_NOT_FOUND);
		responseMsg.setTimeStamp(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		responseMsg.setMessage(errorMessage);
		LOGGER.error(responseMsg.toString());
	} 

}
