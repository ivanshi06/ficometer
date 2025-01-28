package com.model;

import java.time.LocalDate;

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
public class paymentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditAccount_seq")@SequenceGenerator(name = "creditAccount_seq", sequenceName = "creditAccount_sequence", allocationSize = 1)
	private int id;
	private LocalDate paidOn;
	private long amountPaid;
	private LocalDate dueDate;
	private double minimumEmi;
	
	 @OneToOne(mappedBy = "paymentHistory") // Bidirectional relationship pointing back to CreditAccount
	 private CreditAccount creditAccount;
}
