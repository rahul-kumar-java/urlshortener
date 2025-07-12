package com.urlshortener.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.urlshortener.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
		 String errorMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
		
		
		 
		 return new ResponseEntity<>(ErrorResponse.builder()
	              .timestamp(LocalDateTime.now())
	              .status(HttpStatus.BAD_REQUEST.value())
	              .error("Bad Request")
	              .message(errorMsg)
	              .path(request.getRequestURI()).build() , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
		
		return new ResponseEntity<>(ErrorResponse.builder()
	              .timestamp(LocalDateTime.now())
	              .status(HttpStatus.NOT_FOUND.value())
	              .error("Not Found")
	              .message(ex.getMessage())
                  .path(request.getRequestURI())
                  .build(), HttpStatus.NOT_FOUND);
	}
}
