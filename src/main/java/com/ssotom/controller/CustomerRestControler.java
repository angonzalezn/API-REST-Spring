package com.ssotom.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssotom.model.Customer;
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
				:new ResponseEntity<ResponseMessage>(new ResponseMessage("Customer not found"), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping()
	public ResponseEntity<?> create(@RequestBody Customer customer) {
		try {
			Customer newCustomer = customerService.save(customer);
			return new ResponseEntity<Customer>(newCustomer, HttpStatus.CREATED);
		} catch (DataAccessException e) {
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMostSpecificCause().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@PutMapping()
	public ResponseEntity<?> update(@RequestBody Customer customer) {
		try {
			Customer updatedCustomer = customerService.save(customer);
			return new ResponseEntity<Customer>(updatedCustomer, HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMostSpecificCause().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {
			customerService.delete(id);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("customer removed with success"), HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMostSpecificCause().getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

}
