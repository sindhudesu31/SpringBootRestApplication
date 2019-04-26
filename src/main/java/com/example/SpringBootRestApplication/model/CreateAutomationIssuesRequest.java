package com.example.SpringBootRestApplication.model;

import org.springframework.stereotype.Component;

@Component
public class CreateAutomationIssuesRequest {
	private String issue;
	private String resolution;
	private String category;
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	

}
