package com.ssotom.service;

import java.util.List;
import java.util.Optional;

import com.ssotom.model.Customer;

public interface ICustomerService {
	
	public List<Customer> findAll();
	
	public Optional<Customer> findById(Long id);
	
	public Customer save(Customer customer);
	
	public void delete(Long id);
	
	public boolean isValidEmail(Customer customer);

}
