package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CreditAccount;
import com.model.CreditEnquiries;

@Repository
public interface CreditAccountRepo extends JpaRepository<CreditAccount, Integer>{

}
