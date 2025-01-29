package com.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.CreditAccount;
import com.model.CreditEnquiries;
import com.model.Customer;
import com.model.PaymentHistory;
import com.repo.CreditAccountRepo;
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
		
		
		
		//PAYMENT HISTORY SCORE
		int earlyPayments = 0;
	    int delayedPayments = 0;
	    long totalDelayDays = 0;
	    int delayedPaymentsCount = 0;
	    
		Customer customer= customerRepository.findById(uid).get();
		List<CreditAccount> creditAccount=customer.getCreditAccount();
		for(CreditAccount ca:creditAccount) {
			List<PaymentHistory> payments= ca.getPaymentHistory();
			for(PaymentHistory ph:payments) {
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
	   // System.out.println("Payment History Score: " + paymentHistoryScore);
	    
	    
	    
	    
	    
	    //CREDIT-USAGE
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
	    
	    
	    //CREDIT ACCOUNT HISTORY
	    LocalDate currentDate = LocalDate.now();
	    
	    // Calculate the age of each credit account
	    List<Long> accountAges = creditAccount.stream()
	        .map(account -> ChronoUnit.YEARS.between(account.getCreatedAt(), currentDate))
	        .collect(Collectors.toList());

	    double avgAccountAge = accountAges.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
	    double oldestAccountAge = accountAges.stream().mapToDouble(Long::doubleValue).max().orElse(0.0);

	    // Calculate the penalty
	    double penalty = (10 - avgAccountAge / 10) + (5 - oldestAccountAge / 10);

	    // Final Score Calculation
	    double creditAccountHistory = 15 - penalty;
	    
	    
	    
	    //CREDIT MIX 
	    Set<String> uniqueCreditTypes = new HashSet<>();

	    for (CreditAccount account : customer.getCreditAccount()) {
	        if (account.getTypeOfCredit() != null) {
	            uniqueCreditTypes.add(account.getTypeOfCredit().getLabel());
	        }
	    }

	    // Score (D) = Type of credits * 2
	    double creditMix= uniqueCreditTypes.size() * 2;
	    
	    
	    //CREDIT INQUIRIES
	    List<CreditEnquiries> creditEnquiries = customer.getCreditEnquiries();
	    LocalDate oneYearAgo = LocalDate.now().minusYears(1); // Define the recent inquiries period

        // Count the number of inquiries in the last 12 months
        long recentInquiries = creditEnquiries.stream()
                .filter(inquiry -> inquiry.getCheckedOn().isAfter(oneYearAgo))
                .count();

        // Apply the formula: Score (E) = 10 − (recent_inquiries × 2)
        int scoreE = 10 - ((int) recentInquiries * 2);

        // Ensure the score does not go below 0
        double creditInquiries= Math.max(scoreE, 0);
	    
	    
	    
	    
	    
	    
	    
	    
	    double creditScore= paymentHistoryScore + creditUsage + creditAccountHistory + creditMix + creditInquiries;
	    return creditScore ;  // Or any other return type you might need
	}
	
}

