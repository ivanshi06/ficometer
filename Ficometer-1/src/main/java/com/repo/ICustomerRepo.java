package com.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Customer;

@Repository
public interface ICustomerRepo extends JpaRepository<Customer, Integer>{
	Optional<Customer> findByEmail(String email);
}
