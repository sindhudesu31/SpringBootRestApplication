package com.example.SpringBootRestApplication.dao;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.SpringBootRestApplication.exceptions.InvalidInputFault;
import com.example.SpringBootRestApplication.exceptions.IssueAlreadyExistException;
import com.example.SpringBootRestApplication.exceptions.IssueNotFoundException;
import com.example.SpringBootRestApplication.mapper.AutomationIssuesRowMapper;
import com.example.SpringBootRestApplication.model.CreateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;
import com.example.SpringBootRestApplication.model.UpdateAutomationIssuesRequest;

@Repository
@PropertySource(ignoreResourceNotFound=true, value="classpath:sql.properties")
public class AutomationIssuesServiceDaoImpl {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	String createAutomationIssuesSQL="INSERT INTO automation_issue (issue_text, resolution_text, category_text) VALUES (:issue, :resolution, :category) RETURNING automation_issue_identifier";
	
	@Value("${retrieveAutomationIssueSQL}")
	String retrieveAutomationIssueSQL;
	
	@Value("${updateAutomationIssueSQL}")
	String updateAutomationIssueSQL;
	
	@Value("${deleteAutomationIssueSQL}")
	String deleteAutomationIssueSQL;
	
	@Value("${retrieveAutomationIssueByCat}")
	String retrieveAutomationIssueByCategorySQL;
	
	@Value("${retrieveAutomationIssueByIssue}")
	String retrieveAutomationIssueByIssue;
	
	@Autowired
	AutomationIssuesRowMapper automationIssuesRowMapper;
	
	@Transactional
	public Long save(CreateAutomationIssuesRequest automationissue) throws SQLException, IssueNotFoundException, IssueAlreadyExistException{
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		((MapSqlParameterSource) namedParameters).addValue("category", automationissue.getCategory());
		((MapSqlParameterSource) namedParameters).addValue("issue", automationissue.getIssue());
		((MapSqlParameterSource) namedParameters).addValue("resolution", automationissue.getResolution());
		
		int issueAlreadyExists = this.retrieveByIssue(automationissue.getIssue());
		
		if(issueAlreadyExists>0) {
			throw new IssueAlreadyExistException("Issue: "+automationissue.getIssue()+" already exist");
		}
		Long generatedKey = jdbcTemplate.queryForObject(createAutomationIssuesSQL, namedParameters, Long.class);
		return  generatedKey;
	}
	
	@Transactional(readOnly=true)
	public RetrieveAutomationIssuesResponse retrieve(long id) throws IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		try {
			response = (RetrieveAutomationIssuesResponse) jdbcTemplate.
					queryForObject(retrieveAutomationIssueSQL,
					new MapSqlParameterSource("id",id),automationIssuesRowMapper);
		}
		catch(DataAccessException e) {
			throw new IssueNotFoundException("Give issue id: "+id +" does not exist in system.");
		}
		return response;
	}
	
	@Transactional(readOnly=true)
	public List<RetrieveAutomationIssuesResponse> retrieveByCategory(List<String> category) throws SQLException, InvalidInputFault{
		if(category.size()==0) {
			throw new InvalidInputFault("Atleast one category is expected");
		}
		StringBuffer likeQueryPart = new StringBuffer();
		for(int i=0; i<category.size();i++) {
			String cat = category.get(i);
			if(i==0) {
				likeQueryPart.append("category_text like '%"+cat+"%'");
			}
			else {
				likeQueryPart.append(" or category_text like '%"+cat+"%'");
			}
		}
		
		String finalQuery = retrieveAutomationIssueByCategorySQL+likeQueryPart;
		
		List<RetrieveAutomationIssuesResponse> issueres = new ArrayList<RetrieveAutomationIssuesResponse>();
		
		List responseList = jdbcTemplate.query(finalQuery,automationIssuesRowMapper);
		
		for(Object issueResponse: responseList) {
			RetrieveAutomationIssuesResponse issue = (RetrieveAutomationIssuesResponse) issueResponse;
			issueres.add(issue);
		}
		return issueres;
		
	}
	
	@Transactional(readOnly=true)
	public int retrieveByIssue(String issue) throws IssueNotFoundException{
		return jdbcTemplate.queryForObject(retrieveAutomationIssueByIssue, 
				new MapSqlParameterSource("issue",issue),Integer.class);
	}
	
	@Transactional
	public RetrieveAutomationIssuesResponse update(UpdateAutomationIssuesRequest automationissue) throws SQLException, IssueNotFoundException{
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		((MapSqlParameterSource) namedParameters).addValue("id", automationissue.getId());
		((MapSqlParameterSource) namedParameters).addValue("category", automationissue.getCategory());
		((MapSqlParameterSource) namedParameters).addValue("issue", automationissue.getIssue());
		((MapSqlParameterSource) namedParameters).addValue("resolution", automationissue.getResolution());
		
		int numberofrowseffected = jdbcTemplate.update(updateAutomationIssueSQL, namedParameters);
		if(numberofrowseffected == 0) {
			throw new IssueNotFoundException("Given issue id '"+automationissue.getId()+"' does not eixst");
		}
		return this.retrieve(automationissue.getId());
	}
	
	@Transactional
	public Long delete(long id) throws SQLException, IssueNotFoundException {
		int numberOfRowsDeleted = jdbcTemplate.update(deleteAutomationIssueSQL, new MapSqlParameterSource("id", id));
		if(numberOfRowsDeleted==0) {
			throw new IssueNotFoundException("Given issue id '"+id+"' does not exist in system");
		}
		
		return id;
	}
}
