package com.example.SpringBootRestApplication.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.coyote.UpgradeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.SpringBootRestApplication.exceptions.InvalidInputFault;
import com.example.SpringBootRestApplication.exceptions.IssueAlreadyExistException;
import com.example.SpringBootRestApplication.exceptions.IssueNotFoundException;
import com.example.SpringBootRestApplication.model.CreateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.Message;
import com.example.SpringBootRestApplication.model.MessageType;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;
import com.example.SpringBootRestApplication.model.UpdateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.so.AutomationIssuesServiceSO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AutomationIssuesServiceRestController.class)
public class AutomationIssueServiceRestControllerTest {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	AutomationIssuesServiceSO so;
	
	private CreateAutomationIssuesRequest request = new CreateAutomationIssuesRequest();
	private RetrieveAutomationIssuesResponse response = new RetrieveAutomationIssuesResponse();
	private RetrieveAutomationIssuesRequest retriveRequest = new RetrieveAutomationIssuesRequest();
	private UpdateAutomationIssuesRequest update = new UpdateAutomationIssuesRequest();
	
	@InjectMocks
	AutomationIssuesServiceRestController aisrc;
	
	@Autowired
	ObjectMapper mapper;
	
	List<RetrieveAutomationIssuesResponse> listRAIR;
	
	private Message message;
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		request.setCategory("category");
		request.setIssue("issue");
		request.setResolution("resolution");
		
		response.setCategory("category");
		response.setId(100);
		response.setIssue("issue");
		response.setResolution("resolution");
		
		update.setCategory("category");
		update.setId(100);
		update.setIssue("issue");
		update.setResolution("resolution");
		
		listRAIR = new ArrayList<RetrieveAutomationIssuesResponse>();
		listRAIR.add(response);
		
	}
	
	@Test
	public void testCreateAutomationIssue() throws JsonProcessingException, Exception{
		when(so.saveAutomationIssue(any(CreateAutomationIssuesRequest.class))).thenReturn(response);
		this.mvc.perform(
				post("/automationissues/issue").contentType(
						MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request))).andExpect(status().isCreated())
		.andExpect(content().string(mapper.writeValueAsString(response))).andDo(print());
	}
	
	
	@Test
	public void testCreateAutomationIssueThrowException() throws JsonProcessingException, Exception{
		Message msg = new Message.Builder(100, MessageType.ERROR,null).build();
		doThrow(new InvalidInputFault()).when(so).saveAutomationIssue(any(CreateAutomationIssuesRequest.class));
		this.mvc.perform(
				post("/automationissues/issue").contentType(
						MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request))).andExpect(status().isServiceUnavailable())
		.andExpect(content().string(mapper.writeValueAsString(msg))).andDo(print());
	}
	
	@Test
	public void testRetrieveAutomationIssue() throws Exception{
		when(so.retrieveAutomationIssue(1)).thenReturn(response);
		this.mvc.perform(get("/automationissues/issue/{id}",1))
					.andExpect(status().isOk())
					.andExpect(content().string(mapper.writeValueAsString(response)))
					.andDo(print());
		verify(so).retrieveAutomationIssue(1);
	}
	
	@Test
	public void testRetrieveAutomationIssueByCategory() throws Exception {
		when(so.retrieveAutomationIssueByCategory(Arrays.asList("cat","rat"))).thenReturn(listRAIR);
		this.mvc.perform(get("/automationissues/issue/categories/").param("category", new String[] {"cat","rat"}))
		.andExpect(status().isOk())
		.andExpect(content().string(mapper.writeValueAsString(listRAIR)))
		.andDo(print());
		verify(so).retrieveAutomationIssueByCategory(Arrays.asList("cat","rat"));
	}
	
	
	@Test
	public void testDeleteAutomationIssueAndCheckId() throws Exception {
		when(so.deleteAutomationIssue(any(Long.class))).thenReturn(1L);
		this.mvc.perform(get("/automationissues/issue/1"))
		.andExpect(status().isOk())
		.andDo(print());
		
	}
}
