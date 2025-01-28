package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.CreditAccount;
import com.model.Customer;
import com.service.CreditService;
import com.service.CustomerService;

@RestController
@RequestMapping("/")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CreditService creditService;
	
	@PostMapping("/add")
	public Customer add(@RequestBody Customer customer) {
		return customerService.addCustomer(customer);
	}

	@GetMapping("/{id}")
	public Customer findCustomerById(@PathVariable(name = "id") int id) {
		return customerService.getCustomerById(id);

	}
	@DeleteMapping("/deleteById")
	public void deleterCustomerById(@PathVariable(name = "id") int id) {
		customerService.deleteCustomerById(id);
	}
	
	@GetMapping("/all")
	public List<Customer> getAllCustomer() {
		return customerService.getAllCustomer();
	}
	
	@PostMapping("add/credit")
	public ResponseEntity<CreditAccount> addCredit(@RequestBody CreditAccount credit){
		creditService.addCredit(credit);
		return ResponseEntity.ok(credit);
	}
}
