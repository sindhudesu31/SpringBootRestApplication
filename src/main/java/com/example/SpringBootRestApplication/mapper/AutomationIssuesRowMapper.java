package com.example.SpringBootRestApplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;

@Component
public class AutomationIssuesRowMapper implements RowMapper<Object>{
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException{
		RetrieveAutomationIssuesResponse automationIssues = new RetrieveAutomationIssuesResponse();
		automationIssues.setId(rs.getLong("automation_issue_identifier"));
		automationIssues.setCategory(rs.getString("category_text"));
		automationIssues.setResolution(rs.getString("resolution_text"));
		return  automationIssues;
	}
}
