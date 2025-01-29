package com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeOfCredit {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditAccount_seq")@SequenceGenerator(name = "creditAccount_seq", sequenceName = "creditAccount_sequence", allocationSize = 1)
	private int id;
	private String label;
	
	@OneToOne(mappedBy = "typeOfCredit")
	private CreditAccount creditAccount;
	
}
