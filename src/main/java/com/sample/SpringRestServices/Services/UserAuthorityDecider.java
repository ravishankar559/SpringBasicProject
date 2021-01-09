package com.sample.SpringRestServices.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Name;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.DefaultLdapUsernameToDnMapper;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.InetOrgPerson.Essence;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sample.SpringRestServices.DTO.UserCreationRequest;
import com.sample.SpringRestServices.DTO.UserDataHolder;

@Component
@Service
public class UserAuthorityDecider{
	
	private static final String ldapContextSourceURL = "ldap://localhost:10389";
	
	private static final String dnForUser = "dc=springframework,dc=org";
	
	private static final String userDn = "ou=people";
	
	private static final String userId = "uid";
	
	private static final String groupBase = "ou=groups";
	
	@Value("#{${authoritiesMap}}")
	private Map<String,List<String>> authoritiesMap;
	
	public boolean isAuthorizedtocreateuser(Collection<GrantedAuthority> authoritesOfPresentUser, ArrayList<String> authoritiesRequested) {
	 boolean isAuthorized = false;
	 Iterator<GrantedAuthority>	authoritesIterator = authoritesOfPresentUser.iterator();
	 ArrayList<String> authoritesCanBeCreatedByUser = new ArrayList<String>();
	 ArrayList<String> tempauthoriesForUser = null;
	 while(authoritesIterator.hasNext()) {
		 SimpleGrantedAuthority authority = (SimpleGrantedAuthority)authoritesIterator.next();
		 tempauthoriesForUser = (ArrayList<String>) authoritiesMap.get(authority.getAuthority());
		 authoritesCanBeCreatedByUser.addAll(tempauthoriesForUser);
	 }
	 Set<String> uniqueList = new LinkedHashSet<String>();
	 uniqueList.addAll(authoritesCanBeCreatedByUser);
	 authoritesCanBeCreatedByUser.clear();
	 authoritesCanBeCreatedByUser.addAll(uniqueList);
	 if(authoritesCanBeCreatedByUser.containsAll(authoritiesRequested)) {
		 isAuthorized = true; 
	 }
	 return isAuthorized;
	}

	public void createUser(UserCreationRequest userCreationRequest) {
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl(ldapContextSourceURL);
		ldapContextSource.setBase(dnForUser);
		ldapContextSource.afterPropertiesSet();
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String authority : userCreationRequest.getAuthoritiesRequested()){
			authorities.add(new SimpleGrantedAuthority(authority));
		}
		
		//UserDetails newUser = new User(userCreationRequest.getUsername(), userCreationRequest.getPassword(), true, 
		//		true, true, true, authorities);
		//DirContextAdapter adapter = new DirContextAdapter();
		//adapter.setDn(dnForUser);
		//adapter.setAttributeValues("objectclass", new String[] {"top", "person"});
		
		DirContextOperations adapter = new DirContextAdapter();
		//dnForUser = "cn="+userCreationRequest.getUsername()+dnForUser;
		adapter.setAttributeValue("uid", userCreationRequest.getUsername());
		adapter.setAttributeValue("userPassword", userCreationRequest.getPassword());
		adapter.setAttributeValue("cn", userCreationRequest.getCustomerName());
		adapter.setAttributeValue("sn", userCreationRequest.getSurName());
		//adapter.setAttributeValue("authorites", authorities);
		//LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource);
		//ldapTemplate.bind(adapter);
		Essence InetOrgEsssence = new InetOrgPerson.Essence(adapter);
		InetOrgEsssence.setAuthorities(authorities);
		InetOrgPerson newUser = (InetOrgPerson) (InetOrgEsssence)
			      .createUserDetails();
		LdapUserDetailsManager ldapUserDetailsManager = new LdapUserDetailsManager(ldapContextSource);
		ldapUserDetailsManager.setUsernameMapper(new DefaultLdapUsernameToDnMapper(userDn,userId));
		ldapUserDetailsManager.setGroupSearchBase(groupBase);
	    ldapUserDetailsManager.createUser(newUser);
	}
}
