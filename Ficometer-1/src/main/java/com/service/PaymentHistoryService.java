package com.service;

import org.springframework.stereotype.Service;

import com.model.PaymentHistory;

@Service
public interface PaymentHistoryService {
	public PaymentHistory addPayment(PaymentHistory payment);
	public PaymentHistory updatePayment(PaymentHistory payment);
}
