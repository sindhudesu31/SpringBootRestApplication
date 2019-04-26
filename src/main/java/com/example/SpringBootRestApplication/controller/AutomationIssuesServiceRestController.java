package com.example.SpringBootRestApplication.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringBootRestApplication.exceptions.InvalidInputFault;
import com.example.SpringBootRestApplication.exceptions.IssueAlreadyExistException;
import com.example.SpringBootRestApplication.exceptions.IssueNotFoundException;
import com.example.SpringBootRestApplication.exceptions.NoDataFoundFault;
import com.example.SpringBootRestApplication.model.CreateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.model.RetrieveAutomationIssuesResponse;
import com.example.SpringBootRestApplication.model.UpdateAutomationIssuesRequest;
import com.example.SpringBootRestApplication.so.AutomationIssuesServiceSO;

@RestController
@RequestMapping("/automationissues")
public class AutomationIssuesServiceRestController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AutomationIssuesServiceSO so;
	
	@PostMapping(value="/issue")
	public ResponseEntity<RetrieveAutomationIssuesResponse> createAutomationIssue(@RequestBody CreateAutomationIssuesRequest request) throws NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		try {
			response = so.saveAutomationIssue(request);
		}catch(InvalidInputFault e) {
			throw new InvalidInputFault();
		}
		return new ResponseEntity<RetrieveAutomationIssuesResponse>(response, HttpStatus.CREATED);
	}
	
	@GetMapping(value="/issue/{id}")
	@ResponseBody
	public ResponseEntity<RetrieveAutomationIssuesResponse> retrieveAutomationIssue(@PathVariable long id) throws NoDataFoundFault,InvalidInputFault,IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		try {
			response = so.retrieveAutomationIssue(id);
		}
		catch(IssueNotFoundException e) {
			throw new IssueNotFoundException("Given issue id '"+id+"' doesn't exist in system");
		}
		return new ResponseEntity<RetrieveAutomationIssuesResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping(value="/issue/categories")
	@ResponseBody
	public ResponseEntity<List<RetrieveAutomationIssuesResponse>> retrieveAutomationIssueByCategory(@RequestParam("category") List<String> category) throws NoDataFoundFault,InvalidInputFault,IssueNotFoundException{
		List<RetrieveAutomationIssuesResponse> response = null;
		try {
			response = so.retrieveAutomationIssueByCategory(category);
		}
		catch(IssueNotFoundException e) {
			throw new IssueNotFoundException();
		}
		return new ResponseEntity<List<RetrieveAutomationIssuesResponse>>(response, HttpStatus.OK);
	}
	
	@PatchMapping(value="/issue")
	@ResponseBody
	public ResponseEntity<RetrieveAutomationIssuesResponse> updateAutomationIssue(@RequestBody UpdateAutomationIssuesRequest request) throws NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException{
		RetrieveAutomationIssuesResponse response = null;
		try {
			response = so.updateAutomationIssue(request);
		}catch(NoDataFoundFault e) {
			throw new NoDataFoundFault();
		}
		return new ResponseEntity<RetrieveAutomationIssuesResponse>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/issue/{id}")
	public ResponseEntity<String> deleteAutomationIssue(@PathVariable long id) throws NoDataFoundFault, InvalidInputFault, IssueAlreadyExistException, IssueNotFoundException{
		Long deletedId = so.deleteAutomationIssue(id);
		return new ResponseEntity<String>("Automation issue record "+deletedId+" deleted successfully", HttpStatus.OK);
	}
	
}
