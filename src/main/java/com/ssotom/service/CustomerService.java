package com.ssotom.service;

import java.util.List;
import java.util.Optional;

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
		return customerRepository.findAllByOrderByIdAsc();
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
	public boolean isValidEmail(Customer customer) {
		Optional<Customer> oldCustomer = customerRepository.findByEmail(customer.getEmail());
		return oldCustomer.map(cus -> cus.getId() == customer.getId())
				 .orElse(true);	
	}

}
