package com.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CreditAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditAccount_seq")@SequenceGenerator(name = "creditAccount_seq", sequenceName = "creditAccount_sequence", allocationSize = 1)
	private int id;
	private LocalDate createdAt;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="type_credit_id")
	private TypeOfCredit typeOfCredit;
	private long creditLimit;
	private long creditUsed;
//	@OneToMany(mappedBy = "creditAccount", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "payment_id", referencedColumnName = "id") // referencedColumnName points to PaymentHistory's primary key
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	 @JoinColumn(name = "credit_account_id") 
   private List<PaymentHistory> paymentHistory;
}
