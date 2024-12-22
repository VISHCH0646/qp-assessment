package com.example.demo.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GroceryManagementException extends ResponseStatusException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, String> errorMap;

	public GroceryManagementException(String message) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

	public GroceryManagementException(HttpStatus status, String message) {
		super(status, message);
	}

	public GroceryManagementException(HttpStatus status, String message, Map<String, String> errorMap) {
		super(status, message);
		this.errorMap = errorMap;
	}
	
	public Map<String,String> getErrorMap(){
		return errorMap;
	}

}
