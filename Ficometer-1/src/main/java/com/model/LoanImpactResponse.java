package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanImpactResponse {
	private double amount;
    private int term;
    private double interestRate;
    private double predictedImpact;

}
