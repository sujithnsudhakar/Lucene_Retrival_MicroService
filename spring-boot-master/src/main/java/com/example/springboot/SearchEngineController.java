package com.example.springboot;

import org.springframework.web.bind.annotation.RestController;

import com.example.serviceinterface.DocumentServiceIntf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class SearchEngineController {

	@Autowired
	DocumentServiceIntf documentServiceInterface;

	// Test method to check if the server is up and running
	@RequestMapping("/")
	public String test() {
		return "Cool! Server is up and running!";
	}

	/*
	 * Search service not yet implemented will be working on search
	 * functionality after index service
	 */
	@RequestMapping("/search")
	public String searchDocument() {
		return "Wait for sometime! Search functionality coming soon";
	}

	/*
	 * TODO Remove hard coded document path and accept document path from UI
	 * which needs to be indexed from user
	 */
	@RequestMapping("/index")
	public String indexDocuments() {
		String message = documentServiceInterface.createIndex();
		return message;
	}
}
