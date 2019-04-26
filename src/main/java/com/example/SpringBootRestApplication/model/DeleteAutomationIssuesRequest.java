package com.example.SpringBootRestApplication.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class DeleteAutomationIssuesRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	protected long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
