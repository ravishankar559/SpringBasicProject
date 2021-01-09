package com.sample.SpringRestServices.Controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.LdapAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sample.SpringRestServices.DTO.AuthenticationRequest;
import com.sample.SpringRestServices.DTO.RestServiceErrorVo;
import com.sample.SpringRestServices.DTO.RestServiceException;
import com.sample.SpringRestServices.DTO.UserCreationRequest;
import com.sample.SpringRestServices.Services.UserAuthorityDecider;

@ComponentScan(basePackages = "com.sample.SpringRestServices")
@RestController
public class AuthorizationResource {
	
	private static final String NOT_AUTHORIZED = "NOT_AUTHORIZED";
	
	private static final String ALREADY_AUTHENTICTED = "ALREADY_AUTHENTICTED";
	
	@Autowired
	private RestServiceException rsException;
	
	@Autowired
	private RestServiceErrorVo errorVO;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	 @Autowired
	 private RequestParserAndGenerator  requestParserAndGenerator;
	 
	 @Autowired
	 private UserAuthorityDecider userAuthorityDecider;
	
	@RequestMapping(value ="/authentication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<Object> getAuthenticated(@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		if((boolean) session.getAttribute("authStatus")) {
			rsException.setStatus(HttpStatus.FORBIDDEN);
			errorVO.setErrorCode(ALREADY_AUTHENTICTED);
			errorVO.setErrorMessage("Customer already authenticated, please logout and try");
			errorVO.setField("/authorization");
			rsException.setErrors(errorVO);
			return ResponseEntity.status(rsException.getStatus()).body(rsException);
		}
		String taskName = requestParserAndGenerator.getTaskNameFromRequest(request.getRequestURI());
		AuthenticationRequest authenticationRequest = (AuthenticationRequest)requestParserAndGenerator.getRequestFromJson(request,taskName);
		Authentication authentication = null;
		if(null != authenticationRequest && null != authenticationRequest.getUsername() && null != authenticationRequest.getPassword()) {
			try {
				authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			}catch (Exception e) {
			     e.printStackTrace();
			}
		}
		if(authentication == null) {
			rsException.setStatus(HttpStatus.UNAUTHORIZED);
			errorVO.setErrorCode(NOT_AUTHORIZED);
			errorVO.setErrorMessage("Not authenticated user");
			errorVO.setField("/authorization");
			rsException.setErrors(errorVO);
			return ResponseEntity.status(rsException.getStatus()).body(rsException);
		}else {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			request.getSession().setAttribute("authStatus",true);
		}
		return ResponseEntity.ok(authentication.getPrincipal());
	}
	
	@RequestMapping(value ="/logouthandler", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<Object> logoutOperation(@Context HttpServletRequest request,@Context HttpServletResponse response){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)){
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			request.getSession().setAttribute("authStatus",false);
		}else {
			request.getSession().invalidate();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON , produces = MediaType.APPLICATION_JSON )
	public ResponseEntity<Object> createUser(@Context HttpServletRequest request){
		String taskName = requestParserAndGenerator.getTaskNameFromRequest(request.getRequestURI());
		UserCreationRequest userCreationRequest = (UserCreationRequest)requestParserAndGenerator.getRequestFromJson(request,taskName);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken ||
				!userAuthorityDecider.isAuthorizedtocreateuser(
						(Collection<GrantedAuthority>)(((LdapUserDetailsImpl)authentication.getPrincipal()).getAuthorities()),userCreationRequest.getAuthoritiesRequested())) {
			rsException.setStatus(HttpStatus.UNAUTHORIZED);
			errorVO.setErrorCode(NOT_AUTHORIZED);
			errorVO.setErrorMessage("Not authorized to perform operation");
			errorVO.setField("/register");
			rsException.setErrors(errorVO);
			return ResponseEntity.status(rsException.getStatus()).body(rsException);
		}else {
			userAuthorityDecider.createUser(userCreationRequest);
		}
		
		return null;
	}
	
}
