package com.model;

import java.time.LocalDate;
import org.springframework.aot.generate.GeneratedTypeReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditAccount_seq")@SequenceGenerator(name = "creditAccount_seq", sequenceName = "creditAccount_sequence", allocationSize = 1)
	private int id;
	private String name;
	private String email;
	private int age;
	private long mobile;
	
	private String occupation;	
	private double income;
}
