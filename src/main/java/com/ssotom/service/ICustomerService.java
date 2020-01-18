package com.ssotom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssotom.model.Customer;
import com.ssotom.model.Region;

public interface ICustomerService {
	
	public List<Customer> findAll();
	
	public Page<Customer> findAll(Pageable pageable);
	
	public Optional<Customer> findById(Long id);
	
	public Customer save(Customer customer);
	
	public void delete(Long id);
	
	public boolean isValidEmail(Customer customer);
	
	public List<Region> findAllRegions();

}
