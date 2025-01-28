package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Customer;

@Repository
public interface ICustomerRepo extends JpaRepository<Customer, Integer>{
	
}
