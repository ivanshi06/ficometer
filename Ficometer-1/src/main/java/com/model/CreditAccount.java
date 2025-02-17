package com.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate createdAt;
	
	// Use EnumType.STRING to store the enum name in the database (e.g., 'HOME_LOAN')
    @Enumerated(EnumType.STRING)
    private TypeOfCredit typeOfCredit;  // Using Enum instead of int
	private long creditLimit;
	private long creditUsed;
	@Column(unique = true, nullable = false)
	private String creditAccountNumber; // New field for account number

	@Column(nullable = false)
	private String provider; 
//	@OneToMany(mappedBy = "creditAccount", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "payment_id", referencedColumnName = "id") // referencedColumnName points to PaymentHistory's primary key
	//@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	// @JoinColumn(name = "credit_account_id") 
	 @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	 @JsonIgnore
   private List<PaymentHistory> paymentHistory;
}
