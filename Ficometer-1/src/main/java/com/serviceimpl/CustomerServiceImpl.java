package com.serviceimpl;

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
import com.model.TypeOfCredit;
import com.repo.CreditAccountRepo;
import com.repo.ICustomerRepo;
import com.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private ICustomerRepo customerRepository;

	@Override
	public Customer addCustomer(Customer customer) {

		return customerRepository.saveAndFlush(customer);
	}

	@Override
	public Customer getCustomerById(int id) {
		return customerRepository.findById(id).get();
	}

	@Override
	public void deleteCustomerById(int id) {
		customerRepository.deleteById(id);
	}

	@Override
	public List<Customer> getAllCustomer() {
		return customerRepository.findAll();
	}
	public void updateCustomer(Customer customer) {
	    customerRepository.save(customer);
	}

	public double paymentHistoryScore(List<CreditAccount> creditAccounts) {
		int onTimePayments = 0, delayedPayments = 0;
		long totalDelayDays = 0, delayedPaymentsCount = 0;

		for (CreditAccount ca : creditAccounts) {
			for (PaymentHistory ph : ca.getPaymentHistory()) {
				long daysBetween = ChronoUnit.DAYS.between(ph.getDueDate(), ph.getPaidOn());

				if (daysBetween > 0) { // Late payment
					delayedPayments++;
					totalDelayDays += daysBetween;
					delayedPaymentsCount++;
				} else { // On-time payment
					onTimePayments++;
				}
			}
		}
        
		double avgDelayDays = (delayedPaymentsCount > 0) ? (double) totalDelayDays / delayedPaymentsCount : 0;
		double slabPenalty = (avgDelayDays <= 15) ? 0.5 : (avgDelayDays <= 30) ? 1 : (avgDelayDays <= 60) ? 2 : 3;
		double paymentHistoryScore = Math.max(0, 35 - (delayedPayments * slabPenalty)); // Capped at 0

		return paymentHistoryScore;
	}

	public double creditUtilizationScore(List<CreditAccount> creditAccounts) {
		double totalCreditUsed = 0, totalCreditLimit = 0;

		for (CreditAccount ca : creditAccounts) {
			totalCreditUsed += ca.getCreditUsed();
			totalCreditLimit += ca.getCreditLimit();
		}

		double creditUtilized = (totalCreditLimit > 0) ? (totalCreditUsed / totalCreditLimit) * 100 : 0;
		double creditUsageScore = (creditUtilized >= 80) ? 5 : 30 - (creditUtilized / 3); // More penalty for high
																							// utilization
		creditUsageScore = Math.max(0, creditUsageScore); // Ensures non-negative value
		return creditUsageScore;
	}

	public double accountHistoryScore(List<CreditAccount> creditAccounts) {
		LocalDate currentDate = LocalDate.now();
		List<Long> accountAges = creditAccounts.stream()
				.map(account -> ChronoUnit.YEARS.between(account.getCreatedAt(), currentDate))
				.collect(Collectors.toList());

		double avgAccountAge = accountAges.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
		double accountHistoryScore = Math.min(15, (avgAccountAge / 10) * 15); // Score grows with account age

		return accountHistoryScore;
	}
	
	  // This is still autowired for other potential needs, but it's no longer used in this function.

	public double creditMixScore(List<CreditAccount> creditAccounts) {
	    // Get a set of unique typeOfCredit labels from the list of CreditAccounts
	    Set<String> uniqueCreditTypes = creditAccounts.stream()
	            .map(account -> account.getTypeOfCredit().name())  // Convert the enum to its string representation
	            .collect(Collectors.toSet());

	    // Calculate the credit mix score based on the unique types of credit
	    double creditMixScore = Math.min(10, uniqueCreditTypes.size() * 2);  // Max cap at 10

	    return creditMixScore;
	}

	public double creditInquiriesScore(List<CreditAccount>creditAccounts,Customer customer) {
		List<CreditEnquiries> creditEnquiries = customer.getCreditEnquiries();
		long recentInquiries = creditEnquiries.stream()
				.filter(inquiry -> inquiry.getCheckedOn().isAfter(LocalDate.now().minusYears(1))).count();

		double creditInquiriesScore = Math.max(0, 10 - (recentInquiries * 2)); // Prevents negative score
        return creditInquiriesScore;
	}

	@Override
	public double checkCreditScore(int uid) {
		Customer customer = customerRepository.findById(uid).orElse(null);
		if (customer == null)
			return 0; // Handle null case

		List<CreditAccount> creditAccounts = customer.getCreditAccount();
		
		// **1. PAYMENT HISTORY SCORE (35%)**
		
		double paymentHistoryScore = paymentHistoryScore(creditAccounts);

		// **2. CREDIT UTILIZATION SCORE (30%)**

		double creditUsageScore = creditUtilizationScore(creditAccounts);

		// **3. CREDIT ACCOUNT HISTORY SCORE (15%)**
		double accountHistoryScore = accountHistoryScore(creditAccounts);

		// **4. CREDIT MIX SCORE (10%)**
	
		double creditMixScore =  creditMixScore(creditAccounts);

		// **5. CREDIT INQUIRIES SCORE (10%)**
	
		double creditInquiriesScore = creditInquiriesScore(creditAccounts,customer);

		// **Final Credit Score Calculation**
		double creditScore = paymentHistoryScore + creditUsageScore + accountHistoryScore + creditMixScore
				+ creditInquiriesScore;
		return Math.min(100, Math.max(0, creditScore)); // Ensures score is between 0-100
	}
	public String checkCreditHealth(int uid) {
        Customer customer = customerRepository.findById(uid).orElse(null);
        if (customer == null) return "Customer not found";

        List<CreditAccount> creditAccounts = customer.getCreditAccount();

        double paymentScore = paymentHistoryScore(creditAccounts);
        double utilizationScore = creditUtilizationScore(creditAccounts);
        double historyScore = accountHistoryScore(creditAccounts);
        double mixScore = creditMixScore(creditAccounts);
        double inquiriesScore = creditInquiriesScore(creditAccounts,customer);

        StringBuilder warnings = new StringBuilder();

        if (paymentScore < 15) warnings.append("⚠️ Payment history is poor. Improve timely payments.\n");
        if (utilizationScore < 10) warnings.append("⚠️ High credit utilization detected. Reduce spending.\n");
        if (historyScore < 5) warnings.append("⚠️ Short credit history. Maintain old accounts.\n");
        if (mixScore < 5) warnings.append("⚠️ Limited credit mix. Consider diversifying credit types.\n");
        if (inquiriesScore < 5) warnings.append("⚠️ Too many credit inquiries. Avoid excessive loan applications.\n");

        return warnings.length() == 0 ? "✅ Credit health is good." : warnings.toString();
    }

}
