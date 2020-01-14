package com.ssotom.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.ssotom.model.Customer;
import com.ssotom.response.ErrorResponse;
import com.ssotom.response.ResponseMessage;
import com.ssotom.service.ICustomerService;

@CrossOrigin()
@RestController
@RequestMapping("/api/customers")
public class CustomerRestControler {
	
	@Autowired
	private ICustomerService customerService;
	
	@GetMapping()
	public List<Customer> index() {
		return customerService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Optional<Customer> customer = customerService.findById(id);
		return customer.isPresent()
				?new ResponseEntity<Customer>(customer.get(), HttpStatus.OK)
				:new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.NOT_FOUND, "Customer not found", 
						"Customer not found with id = " + id), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping()
	public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result) {
		if(!customerService.isValidEmail(customer)) {
			FieldError error = new FieldError("customer", "email", "email in use");
			result.addError(error);
		}
		if(result.hasErrors()) {
			return returnError(result);
		}
		try {
			Customer newCustomer = customerService.save(customer);
			return new ResponseEntity<Customer>(newCustomer, HttpStatus.CREATED);
		} catch (DataAccessException e) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fatal error", 
					e.getMostSpecificCause().getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@PutMapping()
	public ResponseEntity<?> update(@Valid @RequestBody Customer customer, BindingResult result) {
		return create(customer, result);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {
			customerService.delete(id);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Customer removed with success"), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fatal error", 
					e.getMostSpecificCause().getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	public ResponseEntity<?> returnError(BindingResult result) { 
		List<String> errors = new LinkedList<String>();
		for (FieldError error : result.getFieldErrors()) {
	        errors.add(error.getField() + ": " + error.getDefaultMessage());
	    }
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.BAD_REQUEST, "The request has some errors", 
				errors), HttpStatus.BAD_REQUEST);
	}
	
}
