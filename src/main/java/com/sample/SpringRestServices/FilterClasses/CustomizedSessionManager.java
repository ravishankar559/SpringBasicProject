package com.sample.SpringRestServices.FilterClasses;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ComponentScan(basePackages = "com.sample.SpringRestServices")
@Order(3)
public class CustomizedSessionManager implements Filter{

	private String DOMAIN_NAME = "myapp.com";
	private String SESSION_COOKIE = "SESSIONIDMYAPP";
	private int sessionMaxTimeInSeconds = 10800;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		//doHandleSessionCookie((HttpServletRequest)request ,(HttpServletResponse)response);
		HttpSession session = ((HttpServletRequest) request).getSession(true);
		
		if(null == session.getAttribute("authStatus")) {
			session.setAttribute("authStatus", false);
		}
		filterChain.doFilter(request, response);
	}

	/*private void doHandleSessionCookie(HttpServletRequest request,HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();

		Cookie sessionCookie = null;

		if(null != cookies && cookies.length > 0) {
			for(Cookie cookie : cookies) {
//				if(cookie.getDomain().equals(DOMAIN_NAME) && cookie.getName().equals(SESSION_COOKIE)) {
//					sessionCookie = cookie;
//					break;
//				}
			}
		}
		if(sessionCookie == null) {
			createSessionCookie(request , response);
		}
	}*/

	/*private void createSessionCookie(HttpServletRequest request, HttpServletResponse response) {

		boolean isGeneratedWithRepeatingValues = false;
		char[] randomValue = generateRandomSessionValue();

		int maxTry = 3;
		int tryCount = 1;

		while(tryCount < maxTry) {

			//Check for randomValue with repeating characters
			for(int i=1; i<(randomValue.length)-1; i++) {
				if(randomValue[i] == randomValue[i-1] && randomValue[i] == randomValue[i+1]) {
					isGeneratedWithRepeatingValues = true;
					break;
				}
			}

			isGeneratedWithRepeatingValues = false;
			if(isGeneratedWithRepeatingValues) {
				randomValue = generateRandomSessionValue();
				tryCount++;
			}else {
				// random value generated with no repeating characters.
				break;
			}

		}

		Cookie sessionCookie = new Cookie(SESSION_COOKIE, randomValue.toString());

		sessionCookie.setDomain(DOMAIN_NAME);
		sessionCookie.setMaxAge(sessionMaxTimeInSeconds);
		sessionCookie.setPath("/");
		//sessionCookie.s
		//Adding Session Cookie to the HttpServletResponse Object.
		response.addCookie(sessionCookie);
		//creating the session if not there
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(120);
	}*/

	//To Generate the Random Value.
	/*private char[] generateRandomSessionValue() {

		String allCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_+";
		int size = 14;
		int allCharactersSize = allCharacters.length();
		char[] randomValue = new char[size];

		SecureRandom random = new SecureRandom();

		for(int i=0; i<size; i++) {
			char caseSensitive = allCharacters.charAt(random.nextInt(allCharactersSize));
			if(random.nextBoolean()) {
				caseSensitive = Character.toLowerCase(caseSensitive);
			}
			randomValue[i] = caseSensitive;
		}

		return randomValue;
	}*/

}
