package com.tss.loanEmiSchedular.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_profiles")
@Getter
@Setter
public class FinancialProfile {

    @Id
    private String pan;

    private BigDecimal monthlyIncome;

    private BigDecimal existingDebt;

    private Integer creditScore;
}
