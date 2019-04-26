package com.example.SpringBootRestApplication.so;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.example.SpringBootRestApplication.dao.AutomationIssuesServiceDaoImpl;
import com.example.SpringBootRestApplication.exceptions.InvalidInputFault;
import com.example.SpringBootRestApplication.exceptions.IssueAlreadyExistException;
import com.example.SpringBootRestApplication.exceptions.IssueNotFoundException;
import com.example.SpringBootRestApplication.exceptions.NoDataFoundFault;
import com.example.SpringBootRestApplication.model.CreateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;
import com.example.SpringBootRestApplication.model.UpdateAutomationIssuesRequest;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class AutomationIssuesServiceSOTest {
	RestTemplate testRestTemplate;
	
	@InjectMocks
	AutomationIssuesServiceSO so;
	
	@Mock
	AutomationIssuesServiceDaoImpl dao;
	
	RetrieveAutomationIssuesResponse response;
	
	@Before
	public void setup() {
		response = new RetrieveAutomationIssuesResponse();
		response.setCategory("cat");
		response.setIssue("issue");
		response.setResolution("resolution");
	}
	
	@Test
	public void testSaveAutomationIssueSuccessfully() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException {
		when(dao.save(any(CreateAutomationIssuesRequest.class))).thenReturn(1L);
		when(dao.retrieve(1L)).thenReturn(response);
		RetrieveAutomationIssuesResponse actualResponse = so.saveAutomationIssue(new CreateAutomationIssuesRequest());
		verify(dao).save(Mockito.any(CreateAutomationIssuesRequest.class));
		verify(dao).retrieve(1L);
		Assert.assertEquals(actualResponse.getCategory(),response.getCategory());
	}
	
	@Test(expected=InvalidInputFault.class)
	public void testSaveAutomationIssueNullScenario() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException {
		so.saveAutomationIssue(null);
	}
	
	@Test(expected=IssueAlreadyExistException.class)
	public void testSaveAutomationIssueThrowException() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException{
		doThrow(IssueAlreadyExistException.class).when(dao).save(any(CreateAutomationIssuesRequest.class));
		RetrieveAutomationIssuesResponse actualResponse = so.saveAutomationIssue(new CreateAutomationIssuesRequest());
		verify(dao).save(Mockito.any(CreateAutomationIssuesRequest.class));
	}
	
	@Test(expected=IssueAlreadyExistException.class)
	public void testSaveAutomationIssueThrowSQLException() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException{
		doThrow(SQLException.class).when(dao).save(any(CreateAutomationIssuesRequest.class));
		RetrieveAutomationIssuesResponse actualResponse = so.saveAutomationIssue(new CreateAutomationIssuesRequest());
		verify(dao).save(Mockito.any(CreateAutomationIssuesRequest.class));
	}
	
	@Test
	public void testRetrieveAutomationIssueSuccessfully() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueNotFoundException {
		when(dao.retrieve(Mockito.anyLong())).thenReturn(response);
		RetrieveAutomationIssuesResponse actualResponse = so.retrieveAutomationIssue(100);
		Assert.assertEquals(response.getCategory(), actualResponse.getCategory());
		Mockito.verify(dao).retrieve(Mockito.anyLong());
	}
	
	@Test
	public void testRetrieveAutomationIssueZero() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueNotFoundException {
		RetrieveAutomationIssuesResponse actualresponse = so.retrieveAutomationIssue(0);
		org.junit.Assert.assertNull(actualresponse);
	}
	
	@Test(expected = IssueNotFoundException.class)
	public void testRetrieveAutomationIssueExpectedException() throws IssueNotFoundException, SQLException, NoDataFoundFault, InvalidInputFault {
		Mockito.doThrow(IssueNotFoundException.class).when(dao).retrieve(Mockito.anyLong());
		so.retrieveAutomationIssue(100);
		Mockito.verify(dao).retrieve(Mockito.anyLong());
	}
	
	@Test
	public void testRetrieveAutomationIssueByCategory() throws IssueNotFoundException, SQLException, NoDataFoundFault, InvalidInputFault {
		List<String> categories = new ArrayList<String>();
		List<RetrieveAutomationIssuesResponse> responseArray = new ArrayList<RetrieveAutomationIssuesResponse>();
		responseArray.add(response);
		when(dao.retrieveByCategory(anyList())).thenReturn(responseArray);
		so.retrieveAutomationIssueByCategory(categories);
		Mockito.verify(dao).retrieveByCategory(anyList());
	}
	
	@Test
	public void testRetrieveAutomationIssueByCategoryEmptyString() throws SQLException, NoDataFoundFault, InvalidInputFault, IssueNotFoundException{
		List<RetrieveAutomationIssuesResponse> retrieveAutomationIssuesByCategory = so.retrieveAutomationIssueByCategory(null);
		org.junit.Assert.assertNull(retrieveAutomationIssuesByCategory);
	}
	
	@Test(expected = IssueNotFoundException.class)
	public void testRetrieveAutomationIssueByCategoryThrowException() throws IssueNotFoundException, SQLException, NoDataFoundFault, InvalidInputFault {
		List<String> categories = new ArrayList<String>();
		doThrow(SQLException.class).when(dao).retrieveByCategory(anyList());
		so.retrieveAutomationIssueByCategory(categories);
		Mockito.verify(dao).retrieveByCategory(anyList());
	}
	
	@Test
	public void testUpdateAutomationIssue() throws SQLException, NoDataFoundFault, IssueNotFoundException, InvalidInputFault {
		UpdateAutomationIssuesRequest request = new UpdateAutomationIssuesRequest();
		so.updateAutomationIssue(request);
		verify(dao).update(any(UpdateAutomationIssuesRequest.class));
	}
	
	@Test(expected=IssueNotFoundException.class)
	public void testUpdateAutomationIssueThrowException() throws SQLException, NoDataFoundFault, IssueNotFoundException, InvalidInputFault {
		UpdateAutomationIssuesRequest request = new UpdateAutomationIssuesRequest();
		doThrow(SQLException.class).when(dao).update(any(UpdateAutomationIssuesRequest.class));
		so.updateAutomationIssue(request);
		verify(dao).update(any(UpdateAutomationIssuesRequest.class));
	}
	
	@Test
	public void deletedAutomationIssue() throws SQLException, NoDataFoundFault, IssueNotFoundException, InvalidInputFault {
		Long actualIssueId=100L;
		when(dao.delete(anyLong())).thenReturn(actualIssueId);
		Long deleteIssueId = so.deleteAutomationIssue(anyLong());
		Assert.assertEquals(deleteIssueId,actualIssueId);
		verify(dao).delete(anyLong());
	}
	
	@Test(expected=IssueNotFoundException.class)
	public void deletedAutomationIssueThrowsException() throws SQLException, NoDataFoundFault, IssueNotFoundException, InvalidInputFault {
		doThrow(SQLException.class).when(dao).delete(anyLong());
		so.deleteAutomationIssue(anyLong());
		verify(dao).delete(anyLong());
	}
}
