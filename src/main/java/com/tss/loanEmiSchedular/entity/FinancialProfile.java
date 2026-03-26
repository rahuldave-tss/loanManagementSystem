package com.tss.loanEmiSchedular.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "financial_profiles")
@Getter
@Setter
public class FinancialProfile {

    @Id
    private String pan;

    private Double monthlyIncome;

    private Double existingDebt;

    private Integer creditScore;
}
