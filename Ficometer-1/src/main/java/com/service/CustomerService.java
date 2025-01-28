package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.CreditAccount;
import com.model.Customer;
import com.repo.CreditRepo;
import com.repo.ICustomerRepo;

@Service
public class CustomerService {

	@Autowired
	private ICustomerRepo customerRepository;
	
	public Customer addCustomer(Customer customer) {
	
	return customerRepository.saveAndFlush(customer);
	}
	
	public Customer getCustomerById(int id) {
		return customerRepository.findById(id).get();
	}

	public void deleteCustomerById(int id) {
		customerRepository.deleteById(id);
	}

	public List<Customer> getAllCustomer() {
		return customerRepository.findAll();
	}
	
}

