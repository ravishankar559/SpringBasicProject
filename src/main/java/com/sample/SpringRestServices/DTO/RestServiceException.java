package com.sample.SpringRestServices.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RestServiceException{
	
	private HttpStatus status;
	
	private List<RestServiceErrorVo> error;

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public List<RestServiceErrorVo> getErrors() {
		return error;
	}

	public void setErrors(List<RestServiceErrorVo> error) {
		this.error = error;
	}
	
	public void setErrors(RestServiceErrorVo error) {
		List<RestServiceErrorVo> restServiceErrorVoList = new ArrayList<RestServiceErrorVo>();
		restServiceErrorVoList.add(error);
		this.error = restServiceErrorVoList;
	}
}
