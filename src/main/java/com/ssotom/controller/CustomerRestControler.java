package com.ssotom.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public Optional<Customer> show(@PathVariable Long id) {
		return customerService.findById(id);
	}
	
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public Customer create(@RequestBody Customer customer) {
		return customerService.save(customer);
	}
	
	@PutMapping()
	public Customer update(@RequestBody Customer customer) {
		return customerService.save(customer);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		customerService.delete(id);
	}

}
