package com.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.CreditAccount;
import com.model.TypeOfCredit;
import com.repo.CreditAccountRepo;


@Service
public class CreditServiceImpl {
	@Autowired
	private CreditAccountRepo creditRepository;
	
	// Adding a credit account (for new user )
	public CreditAccount addCredit(CreditAccount credit) {
		return creditRepository.saveAndFlush(credit);
	}

	// Method to find the credit account from creditAccountId
	public CreditAccount getCreditById(int creditAccountId) {
		return creditRepository.findById(creditAccountId).get();
	}

}
