package com.sample.SpringRestServices.FilterClasses;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan(basePackages = "com.sample.SpringRestServices")
@Order(2)
public class XSSFilter implements Filter{
	
	private String defaultHostURL="/myapp/rest";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		filetForXssVulnerability((HttpServletRequest)request , (HttpServletResponse)response);
		
		filterChain.doFilter(request, response);
	}

	private void filetForXssVulnerability(HttpServletRequest request, HttpServletResponse response) {
		if(!request.getRequestURI().startsWith(defaultHostURL)) {
			try {
				response.sendError(404);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
//	@Override
//	public String getParameter(String name) {
//		String validateName = super.getParameter(name);
//		// remove scripts
//		return validateName;
//	}
//	
//	@Override
//	public String[] getParameterValues(String name) {
//		String[] inputvalues = super.getParameterValues(name);
//		// remove scripts
//		return inputvalues;
//	}
}
