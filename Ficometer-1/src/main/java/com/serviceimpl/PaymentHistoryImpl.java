package com.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.CreditAccount;
import com.model.PaymentHistory;
import com.repo.CreditAccountRepo;
import com.repo.PaymentHistoryRepo;
import com.service.PaymentHistoryService;


@Service
public class PaymentHistoryImpl implements PaymentHistoryService{
	
	@Autowired
	private PaymentHistoryRepo paymentRepository;
	
	@Autowired
	private CreditAccountRepo creditAccountRepository;
	
	@Override
	public PaymentHistory addPayment(PaymentHistory payment) {
		payment.setPaidOn(LocalDate.now());
		double minimumEmi = payment.getAmountPaid()/payment.getEmiMonths();
		payment.setMinimumEmi(minimumEmi);
		return paymentRepository.save(payment);
	}

	@Override
	public PaymentHistory updatePayment(PaymentHistory payment) {
		if(paymentRepository.findById(payment.getId())!=null) {
			paymentRepository.save(null);
		}
		return null;
	}
	
	public List<PaymentHistory> getPaymentHistory(int creditAccountId){
		CreditAccount creditAccount=creditAccountRepository.findById(creditAccountId).get();
	      return  creditAccount.getPaymentHistory();
	}
	
	public List<PaymentHistory> getPaymentHistory(String creditAccountNumber){
		CreditAccount creditAccount=creditAccountRepository.findByCreditAccountNumber(creditAccountNumber).get();
	      return  creditAccount.getPaymentHistory();
	}
	
	
	
}
