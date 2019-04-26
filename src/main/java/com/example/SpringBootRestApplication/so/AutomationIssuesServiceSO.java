package com.example.SpringBootRestApplication.so;

import java.security.InvalidAlgorithmParameterException;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.SpringBootRestApplication.dao.AutomationIssuesServiceDaoImpl;
import com.example.SpringBootRestApplication.exceptions.InvalidInputFault;
import com.example.SpringBootRestApplication.exceptions.IssueAlreadyExistException;
import com.example.SpringBootRestApplication.exceptions.IssueNotFoundException;
import com.example.SpringBootRestApplication.exceptions.NoDataFoundFault;
import com.example.SpringBootRestApplication.model.CreateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;
import com.example.SpringBootRestApplication.model.UpdateAutomationIssuesRequest;

@Component
public class AutomationIssuesServiceSO {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String NO_DATA_FOUND_FAULT = "No issue found";
	private static final String ISSUE_ALREADY_EXIST_FAULT = "Issues already exist";
	
	@Autowired
	private AutomationIssuesServiceDaoImpl dao;
	
	public RetrieveAutomationIssuesResponse saveAutomationIssue(
			CreateAutomationIssuesRequest automationissue) throws NoDataFoundFault,InvalidInputFault,IssueAlreadyExistException, IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		if(automationissue != null) {
			try {
				Long id = dao.save(automationissue);
				response = dao.retrieve(id);
			}
			catch(SQLException e) {
				throw new IssueAlreadyExistException(ISSUE_ALREADY_EXIST_FAULT+":");
			}
		}
		else {
			throw new InvalidInputFault("Request cannot be null");
		}
		return response;
	}
	
	public RetrieveAutomationIssuesResponse retrieveAutomationIssue(long id) throws NoDataFoundFault, InvalidInputFault, IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		response = dao.retrieve(id);
		return response;
	}
	
	public List<RetrieveAutomationIssuesResponse> retrieveAutomationIssueByCategory(List<String> category) 
			throws NoDataFoundFault, InvalidInputFault, IssueNotFoundException{
		List<RetrieveAutomationIssuesResponse> response = null;
		if(category != null) {
			try {
				response = dao.retrieveByCategory(category);
			}
			catch(SQLException e) {
				throw new IssueNotFoundException(NO_DATA_FOUND_FAULT+":"+category);
			}
		}
		return response;
	}
	
	public RetrieveAutomationIssuesResponse updateAutomationIssue(UpdateAutomationIssuesRequest automationissue) 
			throws NoDataFoundFault, InvalidInputFault, IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		try {
			response = dao.update(automationissue);
		}
		catch(SQLException e) {
			throw new IssueNotFoundException(NO_DATA_FOUND_FAULT+":"+automationissue.getId());
		}
		return response;
	}
	
	public Long deleteAutomationIssue(long id) throws NoDataFoundFault, InvalidInputFault, IssueNotFoundException {
		Long deletedId = null;
		try {
			deletedId = dao.delete(id);
		}catch(SQLException e) {
			throw new IssueNotFoundException(NO_DATA_FOUND_FAULT+":"+id);
		}
		return deletedId;
	}
}
