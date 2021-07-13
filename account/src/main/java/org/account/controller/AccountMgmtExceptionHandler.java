package org.account.controller;

import org.account.exception.AccountNotFoundException;
import org.account.exception.InvalidParamException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AccountMgmtExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String ERR_MSG_FORMAT = "{ \"msg\" : \"%s\" }";


	// handle resource not found. status code = 404
    @ExceptionHandler(value = {AccountNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundError(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, String.format(ERR_MSG_FORMAT, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {InvalidParamException.class})
    protected ResponseEntity<Object> handleInvalidParamError(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, String.format(ERR_MSG_FORMAT, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
