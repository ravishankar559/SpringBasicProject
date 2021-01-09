package com.sample.SpringRestServices.DTO;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestServiceErrorVo {

	private String errorCode;
	
	private String errorMessage;
	
	private String field;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
}
