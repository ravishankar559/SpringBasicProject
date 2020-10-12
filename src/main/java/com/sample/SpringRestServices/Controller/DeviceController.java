package com.sample.SpringRestServices.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan(basePackages = "com.sample.SpringRestServices")
@RestController
public class DeviceController {
 
	@Autowired
	private RequestParserAndGenerator requestParserAndGenerator;
	
	@RequestMapping(value = "/devices", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON)
	public ResponseEntity getDevices(@Context HttpServletRequest request,
			@RequestParam("Flow") String Flow) {
	   if(null != Flow && requestParserAndGenerator.isValidFlow(request.getRequestURI(),Flow)) {
		   
	   }
	   return ResponseEntity.ok(null);
	}
}
