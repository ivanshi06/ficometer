package com.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.model.Customer;

@Service
public interface CustomerService {

	public Customer addCustomer(Customer customer);

	public Customer getCustomerById(int id);

	public void deleteCustomerById(int id);

	public List<Customer> getAllCustomer();

	public double checkCreditScore(int uid);
}
