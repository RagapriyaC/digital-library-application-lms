package com.raga.library.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * This class is responsible for centralized exception handling for the entire application
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
     * Handler method for MethodArgumentNotValidException when method arguments fail validation
     *
     * @param exception MethodArgumentNotValidException
     * @return ResponseEntity
     */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleDataValidation(MethodArgumentNotValidException exception) {
		Map<String, String> validationErrors = new HashMap<>();
		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			validationErrors.put(fieldName, message);
		});
		return new ResponseEntity<Object>(validationErrors, HttpStatus.BAD_REQUEST); 
	}

	 /**
     * Handler method for ResourceNotFoundException when a requested resource is not found
     *
     * @param exception ResourceNotFoundException .
     * @param request WebRequest 
     * @return ResponseEntity
     */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
}
