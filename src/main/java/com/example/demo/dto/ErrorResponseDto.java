package com.example.demo.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class ErrorResponseDto {

	private String path;
	
	private String errorCode;
	
	private String message;
	
	private String stackTrace;
	
	private Map<String,String> errors;
	
}
