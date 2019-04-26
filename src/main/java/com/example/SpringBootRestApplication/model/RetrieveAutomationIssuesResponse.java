package com.example.SpringBootRestApplication.model;

import org.springframework.stereotype.Component;

@Component
public class RetrieveAutomationIssuesResponse {
	private long id;
	private String issue;
	private String resolution;
	private String category;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	
	@Override
	public String toString() {
		return "RetrieveAutomationIssuesResponse [id=" +id+ ", issue="+ issue+ 
				", resolution=" + resolution+ ", category=" + category +"]";
	}
}
