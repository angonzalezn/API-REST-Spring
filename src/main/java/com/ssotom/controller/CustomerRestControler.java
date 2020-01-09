package com.ssotom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssotom.model.Customer;
import com.ssotom.service.ICustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestControler {
	
	@Autowired
	private ICustomerService customerService;
	
	@GetMapping("/")
	public List<Customer> index() {
		return customerService.findAll();
	}

}
