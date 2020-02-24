package com.fahmy.rest.webservices.restfulwebservices.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fahmy.rest.webservices.restfulwebservices.user.UserNotFoundException;

@RestController
@ControllerAdvice
public class CustomizesResponseEntityExceptionHandler 
extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions (Exception ex,
			WebRequest req){
		ExceptionResponse exceptionResponse = 
		new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundExceptions (UserNotFoundException ex,
			WebRequest req){
		ExceptionResponse exceptionResponse = 
		new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
		
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ExceptionResponse exceptionResponse = 
				new ExceptionResponse(new Date(), "validation failed", ex.getBindingResult().toString());
				
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	
}