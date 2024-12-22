package com.example.demo.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.dto.ErrorResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GroceryManagementExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex.getMessage(), ex);
		var result = ex.getParameterName();
		var errorResponse = buildErrorResponseDto(((ServletWebRequest) request).getRequest().getRequestURI(),
				"Invalid or Missing Parameters", null, Map.of(result, ex.getMessage()));
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error(ex.getLocalizedMessage(), ex);
		var result = ex.getBindingResult();
		var errors = processFieldErrors(result.getFieldErrors());
		var errorResponse = buildErrorResponseDto(((ServletWebRequest) request).getRequest().getRequestURI(),
				"Validation Failed, " + ex.getErrorCount() + "Field Error Occured!", null, errors);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	public ResponseEntity<Object> handleConstraintViolationValidation(ConstraintViolationException ex,
			HttpServletRequest request) {
		log.error(ex.getLocalizedMessage(), ex);
		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String property = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.put(property, message);
		}
		var errorResponse = buildErrorResponseDto(((ServletWebRequest) request).getRequest().getRequestURI(),
				"Validation Failed, " + ex.getConstraintViolations().size() + "Field Error Occured!", null, errors);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler({ GroceryManagementException.class })
	public ResponseEntity<Object> handleBusinessException(GroceryManagementException ex, HttpServletRequest request) {
		log.error(ex.getLocalizedMessage(), ex);
		Map<String, String> errorMap = Optional.ofNullable(ex.getErrorMap())
				.orElse(Map.of("error", ex.getLocalizedMessage()));

		var errorResponse = buildErrorResponseDto(request.getServletPath(), ex.getReason(), null, errorMap);

		return new ResponseEntity<>(errorResponse, ex.getStatusCode());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest request) {
		log.error(ex.getLocalizedMessage(), ex);

		var errorResponse = buildErrorResponseDto(request.getServletPath(),
				"Internal Server error, please try again later!", null, Map.of("error", ex.getLocalizedMessage()));

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Map<String, String> processFieldErrors(List<FieldError> errors) {
		Map<String, String> fieldErrors = new HashMap<>();
		for (FieldError error : errors) {
			fieldErrors.put(error.getField(), error.getDefaultMessage());
		}

		return fieldErrors;
	}

	private ErrorResponseDto buildErrorResponseDto(String path, String message, String stackTrace,
			Map<String, String> errors) {
		return ErrorResponseDto.builder().path(path).message(message).stackTrace(stackTrace).errors(errors).build();
	}

}
