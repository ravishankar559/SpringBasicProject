package com.sample.SpringRestServices.Controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sample.SpringRestServices.DTO.Employee;
import com.sample.SpringRestServices.DTO.EmployeeRepository;
import com.sample.SpringRestServices.DTO.RestServiceException;

@ComponentScan(basePackages = "com.sample.SpringRestServices")
@RestController
public class EmployeeController {

  @Autowired
  private EmployeeRepository repository;
  
  @Autowired
  private RequestParserAndGenerator  requestParserAndGenerator;

//  EmployeeController(EmployeeRepository repository) {
//    this.repository = repository;
//  }

  // Aggregate root
  @RequestMapping(value ="/employees", method = RequestMethod.GET)
  public ResponseEntity getAllEmployees(@Context HttpServletRequest request) {
	  List<Employee> employees = null;
	  try {
		  employees = repository.findAll();
	  }catch(Throwable e) {
		RestServiceException rsException = new RestServiceException();
		rsException.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		throw rsException;
	  }
    return ResponseEntity.ok(employees);
  }
  
  @RequestMapping(value ="/employee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public ResponseEntity newEmployee(@Context HttpServletRequest request) {
	  String taskName = requestParserAndGenerator.getTaskNameFromRequest(request.getRequestURI());
	  Employee employee = (Employee) requestParserAndGenerator.getRequestFromJson(request,taskName);
	  Employee savedEmployee = repository.save(employee);
      return ResponseEntity.status(201).body(savedEmployee);
  }

  // Single item
  @RequestMapping(value ="/employees/{employeeid}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public ResponseEntity getEmployee(@Context HttpServletRequest request,@PathVariable("employeeid") String employeeId) {
	   Optional<Employee>  requestedEmployee = repository.findById(Long.parseLong(employeeId));
       if(requestedEmployee.isPresent()) {
    	   return ResponseEntity.ok(requestedEmployee);
       }else {
    	   RestServiceException rsException = new RestServiceException();
   		   rsException.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
   		   throw rsException;
       }
  }

  @RequestMapping(value ="/employees/{employeeid}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public ResponseEntity editEmployee(@Context HttpServletRequest request,@PathVariable("employeeid") String employeeId) {
	  
	  String taskName = requestParserAndGenerator.getTaskNameFromRequest(request.getRequestURI());
	  Employee employee = (Employee) requestParserAndGenerator.getRequestFromJson(request,taskName);
	  Optional<Employee> employeeToBeModified = repository.findById(Long.parseLong(employeeId));
	  Employee savedEmployee = null;
	  
	  if(employeeToBeModified.isPresent()){
		  Employee OldEmployee = employeeToBeModified.get();
		  OldEmployee.setName(employee.getName());
		  OldEmployee.setRole(employee.getRole());
		  savedEmployee = repository.save(OldEmployee);
	  }else {
		  savedEmployee = repository.save(employee);
	  }
	  return ResponseEntity.ok(savedEmployee);
  }

  @RequestMapping(value ="/employees/{employeeid}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON)
  public ResponseEntity deleteEmployee(@PathVariable("employeeid") String employeeId) {
	  repository.deleteById(Long.parseLong(employeeId));
	  return ResponseEntity.status(204).body(null);
  }
}