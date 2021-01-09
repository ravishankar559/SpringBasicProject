package com.sample.SpringRestServices.DTO;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

public class UserDataHolder extends InetOrgPerson{
	
	public void doPopulateContext(DirContextAdapter adapter) {
		populateContext(adapter);
	}
	
	@Override
	protected void populateContext(DirContextAdapter adapter) {
		// TODO Auto-generated method stub
		super.populateContext(adapter);
	}
	
}
