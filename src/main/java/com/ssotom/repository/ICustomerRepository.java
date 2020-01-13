package com.ssotom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssotom.model.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
	
	 public List<Customer> findAllByOrderByIdAsc();
	
}
