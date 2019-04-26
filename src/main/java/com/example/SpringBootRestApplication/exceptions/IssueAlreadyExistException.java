package com.example.SpringBootRestApplication.exceptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IssueAlreadyExistException extends Exception{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(IssueAlreadyExistException.class);
	private static final String ISSUE_ALREADY_IN_SYSTEM="Issue already registered";
	private final ResponseExcpetionMessage responseMsg;
	
	public IssueAlreadyExistException() {
		this(ISSUE_ALREADY_IN_SYSTEM);
	}
	
	public IssueAlreadyExistException(String errorMessage) {
		super(errorMessage);
		responseMsg = new ResponseExcpetionMessage();
		responseMsg.setTitle(ISSUE_ALREADY_IN_SYSTEM);
		responseMsg.setTimeStamp(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
		responseMsg.setMessage(errorMessage);
		LOGGER.error(responseMsg.toString());
	}
	
}
