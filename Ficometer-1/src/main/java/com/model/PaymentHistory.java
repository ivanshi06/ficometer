package com.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditAccount_seq")@SequenceGenerator(name = "creditAccount_seq", sequenceName = "creditAccount_sequence", allocationSize = 1)
	private int id;
	private LocalDate paidOn;
	private long amountPaid;
	private LocalDate dueDate;
	private double minimumEmi;
	private int emiMonths;
	/*
	 * @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name = "credit_account_id") private CreditAccount creditAccount;
	 */
}
