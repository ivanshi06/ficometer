package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.CreditAccount;
import com.repo.CreditAccountRepo;

@Service
public class CreditService {
	@Autowired
	private CreditAccountRepo creditRepository;
	public CreditAccount addCredit(CreditAccount credit) {
		System.out.println("-----------------------------------");
		System.out.println(credit.toString());
		System.out.println("-----------------------------------");
		return creditRepository.saveAndFlush(credit);
	}
}
