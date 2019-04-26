package com.example.SpringBootRestApplication.exceptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Component;

@Component
public class ResponseExcpetionMessage {
	private String timeStamp;
	private String title;
	private String message;
	
	public ResponseExcpetionMessage(String msg) {
		this.message = msg;
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());				
	}
	
	public ResponseExcpetionMessage() {
		
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
