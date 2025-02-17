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
import com.model.CreditEnquiries;
import com.model.Customer;
import com.model.PaymentHistory;
import com.serviceimpl.CreditServiceImpl;
import com.serviceimpl.CustomerServiceImpl;
import com.serviceimpl.PaymentHistoryImpl;

@RestController
@RequestMapping("/")
public class CustomerController {
	@Autowired
	private CustomerServiceImpl customerService;

	@Autowired
	private CreditServiceImpl creditService;
	
	@Autowired
	private PaymentHistoryImpl paymentHistory;

	@PostMapping("/add")
	public Customer add(@RequestBody Customer customer) {
		return customerService.addCustomer(customer);
	}

	@GetMapping("/{id}")
	public Customer findCustomerById(@PathVariable(name = "id") int id) {
		return customerService.getCustomerById(id);

	}

	@DeleteMapping("/deleteById/{id}")
	public void deleterCustomerById(@PathVariable(name = "id") int id) {
		customerService.deleteCustomerById(id);
	}

	@GetMapping("/all")
	public List<Customer> getAllCustomer() {
		return customerService.getAllCustomer();
	}
////////////////////////////////////////////CREDIT 
	@PostMapping("/add/credit/{customerId}")
	public ResponseEntity<CreditAccount> addCredit(@PathVariable int customerId, @RequestBody CreditAccount creditAccount) {
	    Customer customer = customerService.getCustomerById(customerId);
	    if (customer == null) {
	        return ResponseEntity.notFound().build();
	    }

	    // Associate CreditAccount with the Customer
	    creditAccount = creditService.addCredit(creditAccount);
	    customer.getCreditAccount().add(creditAccount);
	    customerService.updateCustomer(customer); // Ensure customer gets updated in DB

	    return ResponseEntity.ok(creditAccount);
	}

	@GetMapping("/score/{uid}")
	public ResponseEntity<Double> getTotalCreditScore(@PathVariable int uid) {
		return ResponseEntity.ok(customerService.checkCreditScore(uid));
	}
	
	@PostMapping("/add/credit-enquiry/{customerId}")
	public ResponseEntity<Customer> addCreditEnquiry(@PathVariable int customerId, @RequestBody CreditEnquiries enquiry) {
	    Customer customer = customerService.getCustomerById(customerId);
	    if (customer == null) {
	        return ResponseEntity.notFound().build();
	    }
	    customer.getCreditEnquiries().add(enquiry);
	    customerService.addCustomer(customer);
	    return ResponseEntity.ok(customer);
	}

	@PostMapping("/update/payment-history/{creditAccountId}")
	public ResponseEntity<CreditAccount> updatePaymentHistory(@PathVariable int creditAccountId, @RequestBody PaymentHistory payment) {
	    CreditAccount creditAccount = creditService.getCreditById(creditAccountId);
	    if (creditAccount == null) {
	        return ResponseEntity.notFound().build();
	    }
	    creditAccount.getPaymentHistory().add(payment);
	    creditService.addCredit(creditAccount);
	    return ResponseEntity.ok(creditAccount);
	}

	

	@GetMapping("/payment-history/{uid}")
	public ResponseEntity<Double> getPaymentHistoryScore(@PathVariable int uid) {
		Customer customer = customerService.getCustomerById(uid);
		if (customer == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(customerService.paymentHistoryScore(customer.getCreditAccount()));
	}
	
	@GetMapping("/payment-history/{creditAccountId}")  //accountid
	public ResponseEntity<List<PaymentHistory>> displayPaymentHistory(@PathVariable int creditAccountId) {
		//Customer customer = customerService.getCustomerById(aid);
		List<PaymentHistory> ca=paymentHistory.getPaymentHistory(creditAccountId);
		if (ca == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(ca);
	}
	
	@GetMapping("/payment-history/{creditAccountNumber}")  //accountid
	public ResponseEntity<List<PaymentHistory>> displayPaymentHistory(@PathVariable String creditAccountId) {
		//Customer customer = customerService.getCustomerById(aid);
		List<PaymentHistory> ca=paymentHistory.getPaymentHistory(creditAccountId);
		if (ca == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(ca);
	}
	

	@GetMapping("/credit-utilization/{uid}")
	public ResponseEntity<Double> getCreditUtilizationScore(@PathVariable int uid) {
		Customer customer = customerService.getCustomerById(uid);
		if (customer == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(customerService.creditUtilizationScore(customer.getCreditAccount()));
	}

	@GetMapping("/credit-health/{uid}")
	public ResponseEntity<String> getCreditHealthWarning(@PathVariable int uid) {
		return ResponseEntity.ok(customerService.checkCreditHealth(uid));
	}
}
