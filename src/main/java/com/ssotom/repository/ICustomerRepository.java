package com.ssotom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssotom.model.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
	
}
