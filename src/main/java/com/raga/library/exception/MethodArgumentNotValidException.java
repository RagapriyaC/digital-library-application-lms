package com.raga.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class is responsible for MethodArgumentNotValidException when method
 * arguments fail validation.
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MethodArgumentNotValidException extends Exception {

	private static final long serialVersionUID = 1L;
	
	BindingResult bindingResult;

	public MethodArgumentNotValidException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return this.bindingResult;
	}

}
