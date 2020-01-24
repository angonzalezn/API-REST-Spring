package com.ssotom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ssotom.model.Customer;
import com.ssotom.model.Region;
import com.ssotom.repository.ICustomerRepository;

@Service
public class CustomerService implements ICustomerService{
	
	@Autowired
	private ICustomerRepository customerRepository;

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAllByOrderByIdAsc();
	}
	
	@Override
	public Page<Customer> findAll(Pageable pageable) {
		return customerRepository.findAllByOrderByIdAsc(pageable);
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public Customer save(Customer customer)  {
		return customerRepository.save(customer);	
	}

	@Override
	public void delete(Long id) {
		customerRepository.deleteById(id);	
	}

	@Override
	public Boolean existsByEmail(String email) {
		return customerRepository.existsByEmail(email);
	}

	@Override
	public List<Region> findAllRegions() {
		return customerRepository.findAllRegions();
	}

}
