package com.sample.SpringRestServices.DTO;

import java.util.ArrayList;

public class UserCreationRequest implements RequestDTO{
	
	private String username;
	
	private String password;
	
	private ArrayList<String> authoritiesRequested;
	
	private String customerName;
	
	private String surName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getAuthoritiesRequested() {
		return authoritiesRequested;
	}

	public void setAuthoritiesRequested(ArrayList<String> authoritiesRequested) {
		this.authoritiesRequested = authoritiesRequested;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

}
