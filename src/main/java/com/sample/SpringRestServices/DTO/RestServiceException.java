package com.sample.SpringRestServices.DTO;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.springframework.http.HttpStatus;

public class RestServiceException extends WebApplicationException{
	
	private HttpStatus status;
	
	private List<RestServiceErrorVo> errors;

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public List<RestServiceErrorVo> getErrors() {
		return errors;
	}

	public void setErrors(List<RestServiceErrorVo> errors) {
		this.errors = errors;
	}

	public RestServiceException() {
		//Empty Constructor
	}
}
