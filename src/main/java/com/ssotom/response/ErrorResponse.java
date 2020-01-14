package com.ssotom.response;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private List<String> errors;
 
    public ErrorResponse(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
 
    public ErrorResponse(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getErrors() {
		return errors;
	}
     
}
