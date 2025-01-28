package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CreditAccount;

@Repository
public interface CreditRepo extends JpaRepository<CreditAccount, Integer>{

}
