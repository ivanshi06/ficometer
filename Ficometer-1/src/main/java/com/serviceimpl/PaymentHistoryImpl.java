package com.serviceimpl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.PaymentHistory;
import com.repo.PaymentHistoryRepo;
import com.service.PaymentHistoryService;

@Service
public class PaymentHistoryImpl implements PaymentHistoryService{
	
	@Autowired
	private PaymentHistoryRepo paymentRepository;
	
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
			paymentRepository.save(null)
		}
		return null;
	}
	
	
}
