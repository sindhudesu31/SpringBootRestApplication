package com.example.SpringBootRestApplication.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.example.SpringBootRestApplication.exceptions.InvalidInputFault;
import com.example.SpringBootRestApplication.exceptions.IssueAlreadyExistException;
import com.example.SpringBootRestApplication.exceptions.IssueNotFoundException;
import com.example.SpringBootRestApplication.mapper.AutomationIssuesRowMapper;
import com.example.SpringBootRestApplication.model.CreateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;
import com.example.SpringBootRestApplication.model.UpdateAutomationIssuesRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AutomationIssueServiceDaoTest {
	
	@Mock
	NamedParameterJdbcTemplate jdbcTemplate;
	
	@InjectMocks
	AutomationIssuesServiceDaoImpl dao;
	
	@Captor
	ArgumentCaptor<SqlParameterSource> sqlParamsCaptor;
	
	@Mock
	private MapSqlParameterSource sqlParametersSource;
	
	@Mock
	RetrieveAutomationIssuesResponse resp;
	
	@Mock
	GeneratedKeyHolder keyHolder;
	
	@Mock
	private RowMapper<Long> mockRowMapper;
	
	RetrieveAutomationIssuesResponse expectedResponse;
	
	List<RetrieveAutomationIssuesResponse> expectedResponseList;
	
	UpdateAutomationIssuesRequest updateAutomationIssueRequest;
	
	@Before
	public void createMocks() {
		dao = new AutomationIssuesServiceDaoImpl();
		MockitoAnnotations.initMocks(this);
		expectedResponse = new RetrieveAutomationIssuesResponse();
		expectedResponse.setId(100L);
		expectedResponseList = new ArrayList<RetrieveAutomationIssuesResponse>();
		expectedResponseList.add(expectedResponse);
		updateAutomationIssueRequest = new UpdateAutomationIssuesRequest();
		updateAutomationIssueRequest.setId(100L);
	}
	
	@Test
	public void testSave() throws SQLException, IssueNotFoundException, IssueAlreadyExistException {
		Long actualValue = 1L;
		when(
				jdbcTemplate.queryForObject(anyString(),
						any(SqlParameterSource.class),eq(Long.class))).thenReturn(actualValue);
		
		when(
				jdbcTemplate.queryForObject(anyString(),
						any(SqlParameterSource.class),eq(Integer.class))).thenReturn(0);
		
		Long generatedKey = dao.save(new CreateAutomationIssuesRequest());
		
		verify(jdbcTemplate).queryForObject(anyString(),
				sqlParamsCaptor.capture(), eq(Long.class));
		
		Assert.assertEquals(generatedKey,actualValue);
		SqlParameterSource parms = sqlParamsCaptor.getValue();
		assertTrue(parms.hasValue("category"));
		assertTrue(parms.hasValue("issue"));
		assertTrue(parms.hasValue("resolution"));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = IssueAlreadyExistException.class)
	public void testSaveException() throws SQLException, IssueNotFoundException, IssueAlreadyExistException {
		when(
				jdbcTemplate.queryForObject(anyString(),
						any(SqlParameterSource.class),eq(Integer.class))).thenReturn(1);
		dao.save(new CreateAutomationIssuesRequest());
	}
	
	@Test
	public void testRetrive() throws IssueNotFoundException {
		when(
				jdbcTemplate.queryForObject(anyString(),
						any(SqlParameterSource.class), any(AutomationIssuesRowMapper.class))).thenReturn(expectedResponse);
		RetrieveAutomationIssuesResponse actualResponse = dao.retrieve(100L);
		verify(jdbcTemplate).queryForObject(anyString(),
				any(SqlParameterSource.class), any(AutomationIssuesRowMapper.class));
		
		assertEquals(expectedResponse.getId(), actualResponse.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = IssueNotFoundException.class)
	public void testRetriveExcpetion() throws IssueNotFoundException{
		when(
				jdbcTemplate.queryForObject(anyString(),
						any(SqlParameterSource.class), any(AutomationIssuesRowMapper.class))).thenThrow(IssueNotFoundException.class);
		dao.retrieve(100L);
		verify(jdbcTemplate).queryForObject(anyString(),
				any(SqlParameterSource.class), any(AutomationIssuesRowMapper.class));
	}
	
	@Test(expected = InvalidInputFault.class)
	public void testRetrieveByCategoryWithNoCategory() throws SQLException, InvalidInputFault{
		List<String> categories = new ArrayList<String>();
		List<RetrieveAutomationIssuesResponse> actualResponseList = dao.retrieveByCategory(categories);
	}
	
	@Test
	public void testRetrieveByCategoryWithOneCategory() throws SQLException, InvalidInputFault{
		List<String> categories = new ArrayList<String>();
		categories.add("cat");
		doReturn(expectedResponseList).when(jdbcTemplate).query(anyString(),any(AutomationIssuesRowMapper.class));
		List<RetrieveAutomationIssuesResponse> actualResponseList = dao.retrieveByCategory(categories);
		assertEquals(expectedResponseList.get(0).getId(), actualResponseList.get(0).getId());
		verify(jdbcTemplate).query(anyString(),
				any(AutomationIssuesRowMapper.class));
	}
	
	@Test
	public void testRetrieveByCategoryWithMulCategory() throws SQLException, InvalidInputFault{
		List<String> categories = new ArrayList<String>();
		categories.add("cat");
		categories.add("bat");
		categories.add("mat");
		doReturn(expectedResponseList).when(jdbcTemplate).query(anyString(),any(AutomationIssuesRowMapper.class));
		List<RetrieveAutomationIssuesResponse> actualResponseList = dao.retrieveByCategory(categories);
		assertEquals(expectedResponseList.get(0).getId(), actualResponseList.get(0).getId());
		verify(jdbcTemplate).query(anyString(),
				any(AutomationIssuesRowMapper.class));
	}
	
	@Test
	public void testUpdate() throws  SQLException, IssueNotFoundException{
		when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);
		dao.update(updateAutomationIssueRequest);
		verify(jdbcTemplate).update(anyString(), sqlParamsCaptor.capture());
		SqlParameterSource parms = sqlParamsCaptor.getValue();
		assertTrue(parms.hasValue("id"));
		assertTrue(parms.hasValue("category"));
		assertTrue(parms.hasValue("issue"));
		assertTrue(parms.hasValue("resolution"));
	}
	
	@Test(expected = IssueNotFoundException.class)
	public void testUpdateIfRecordDoesNotExist() throws SQLException, IssueNotFoundException{
		when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);
		dao.update(updateAutomationIssueRequest);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = SQLException.class)
	public void testUpdateThrowException() throws  SQLException, IssueNotFoundException{
		when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenThrow(SQLException.class);
		dao.update(updateAutomationIssueRequest);
		verify(jdbcTemplate).update(anyString(), sqlParamsCaptor.capture());
		SqlParameterSource parms = sqlParamsCaptor.getValue();
		assertTrue(parms.hasValue("id"));
		assertTrue(parms.hasValue("category"));
		assertTrue(parms.hasValue("issue"));
		assertTrue(parms.hasValue("resolution"));
	}
	
	@Test
	public void testDelete() throws SQLException, IssueNotFoundException{
		when(jdbcTemplate.update(anyString(),any(SqlParameterSource.class))).thenReturn(1);
		Long expectedID = 100L;
		Long actualID = dao.delete(100L);
		assertEquals(expectedID, actualID);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=SQLException.class)
	public void testDeleteException() throws SQLException, IssueNotFoundException{
		when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenThrow(SQLException.class);
		dao.delete(100L);
	}
	
	@Test(expected=IssueNotFoundException.class)
	public void testDeleteExceptionNoRecord() throws SQLException, IssueNotFoundException{
		when(jdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);
		dao.delete(100L);
	}
	
	
}
