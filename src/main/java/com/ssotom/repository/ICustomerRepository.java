package com.ssotom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ssotom.model.Customer;
import com.ssotom.model.Region;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {
	
	 public List<Customer> findAllByOrderByIdAsc();
	 
	 public Page<Customer> findAllByOrderByIdAsc(Pageable pePageable);
	 
	 public Optional<Customer> findByEmail(String email);
	 
	 @Query("from Region")
	 public List<Region> findAllRegions();
	
}
