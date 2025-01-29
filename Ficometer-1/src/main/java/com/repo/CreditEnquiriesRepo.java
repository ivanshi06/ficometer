package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CreditEnquiries;
@Repository
public interface CreditEnquiriesRepo extends JpaRepository<CreditEnquiries, Integer> {

}
