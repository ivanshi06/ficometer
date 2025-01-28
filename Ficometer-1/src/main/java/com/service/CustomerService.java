package com.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.CreditAccount;
import com.model.Customer;
import com.model.paymentHistory;
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
	
	public double checkCreditScore (int uid)
	{
		
		
		
		
		int earlyPayments = 0;
	    int delayedPayments = 0;
	    long totalDelayDays = 0;
	    int delayedPaymentsCount = 0;
	    
		Customer customer= customerRepository.findById(uid).get();
		List<CreditAccount> creditAccount=customer.getCreditAccount();
		for(CreditAccount ca:creditAccount) {
			List<paymentHistory> payments= ca.getPaymentHistory();
			for(paymentHistory ph:payments) {
				long daysBetween = ChronoUnit.DAYS.between(ph.getPaidOn(), ph.getDueDate());
				
				if (daysBetween > 0) {
	                // Payment is delayed
	                delayedPayments++;
	                totalDelayDays += daysBetween;  // Accumulate delay days
	                delayedPaymentsCount++;
	            }
				else if (daysBetween < 0) {
	                // Payment is early
	                earlyPayments++;
	            }
			}
		}
		double avgDelayDays = delayedPaymentsCount > 0 ? (double) totalDelayDays / delayedPaymentsCount : 0;
		
		  // Define penalty slab based on average delay
	    double slabPenalty;
	    if (avgDelayDays <= 15) {
	        slabPenalty = 0.5;
	    } else if (avgDelayDays <= 30) {
	        slabPenalty = 1;
	    } else if (avgDelayDays <= 60) {
	        slabPenalty = 2;
	    } else {
	        slabPenalty = 3;
	    }
	 
	    // Calculate the payment history score
	    double paymentHistoryScore = 35 - (delayedPayments * slabPenalty) + (earlyPayments * 0.2);
	    // Optionally, you can return the score or just print it for now.
	    System.out.println("Payment History Score: " + paymentHistoryScore);
	    
	    
	    
	    
	    
	    
	    double totalCreditUsed = 0;
	    double totalCreditLimit = 0;
	    
	    // Sum up the creditUsed and creditLimit for all CreditAccounts of the customer
	    for (CreditAccount creditAccount2 : creditAccount) {
	        totalCreditUsed += creditAccount2.getCreditUsed();
	        totalCreditLimit += creditAccount2.getCreditLimit();
	    }
	    
	    // Ensure there's no division by zero
	    if (totalCreditLimit == 0) {
	        return 30; // If credit limit is 0, return the highest possible score
	    }
	    
	    // Calculate the credit utilization
	    double creditUtilized = (totalCreditUsed / totalCreditLimit) * 100;
	    
	    // Calculate Score B
	    double creditUsage = 30 - (creditUtilized / 100 * 30);
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    int creditScore= paymentHistoryScore + creditUsage + creditAccountHistory + creditMix + creditInquiries;
	    return creditScore ;  // Or any other return type you might need
	}
	
}

