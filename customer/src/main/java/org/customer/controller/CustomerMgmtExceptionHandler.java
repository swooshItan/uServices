package org.customer.controller;

import java.util.HashMap;
import java.util.Map;

import org.customer.exception.CustomerNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomerMgmtExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String ERR_MSG_FORMAT = "{ \"msg\" : \"%s\" }";


	// handle resource not found. status code = 404
    @ExceptionHandler(value = {CustomerNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundError(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, String.format(ERR_MSG_FORMAT, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // handle input parameters validation errors. status code = 400
    @Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, headers, status);
	}
}
