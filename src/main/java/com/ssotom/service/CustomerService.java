package com.ssotom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssotom.model.Customer;
import com.ssotom.repository.ICustomerRepository;

@Service
public class CustomerService implements ICustomerService{
	
	@Autowired
	private ICustomerRepository customerRepository;

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

}
