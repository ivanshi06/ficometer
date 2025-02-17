package com.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CreditAccount;
import com.model.CreditEnquiries;
import com.model.PaymentHistory;

@Repository
public interface CreditAccountRepo extends JpaRepository<CreditAccount, Integer>{

	Optional<CreditAccount> findByCreditAccountNumber(String creditAccountNumber);

}
