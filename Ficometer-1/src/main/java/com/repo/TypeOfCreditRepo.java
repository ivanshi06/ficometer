package com.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.TypeOfCredit;

@Repository
public interface TypeOfCreditRepo extends JpaRepository<TypeOfCredit, Integer> {

}
