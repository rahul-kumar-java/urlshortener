package com.urlshortener.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.urlshortener.controller.UrlShortenerController;
import com.urlshortener.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
		 String errorMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
		
		 logger.debug("Input validation failed for Long Url: status={}, error={}, message={}", HttpStatus.BAD_REQUEST.value(), "Bad Request", errorMsg);
		 
		 return new ResponseEntity<>(ErrorResponse.builder()
	              .timestamp(LocalDateTime.now())
	              .status(HttpStatus.BAD_REQUEST.value())
	              .error("Bad Request")
	              .message(errorMsg)
	              .path(request.getRequestURI()).build() , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
		
		logger.debug("Short Code is not found: status={}, error={} ,message={}", HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());
		return new ResponseEntity<>(ErrorResponse.builder()
	              .timestamp(LocalDateTime.now())
	              .status(HttpStatus.NOT_FOUND.value())
	              .error("Not Found")
	              .message(ex.getMessage())
                  .path(request.getRequestURI())
                  .build(), HttpStatus.NOT_FOUND);
	}
	
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleValidationError(IllegalArgumentException ex, HttpServletRequest request) {
		 
		 String errorMsg = ex.getMessage();
		 logger.debug("Input validation failed for short code: status={}, error={}, message={}", HttpStatus.BAD_REQUEST.value(), "Bad Request", errorMsg);
		 return new ResponseEntity<>(ErrorResponse.builder()
	              .timestamp(LocalDateTime.now())
	              .status(HttpStatus.BAD_REQUEST.value())
	              .error("Bad Request")
	              .message(errorMsg)
	              .path(request.getRequestURI()).build() , HttpStatus.BAD_REQUEST);
	}
	
}
