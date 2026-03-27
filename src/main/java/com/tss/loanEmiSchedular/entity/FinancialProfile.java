package com.tss.loanEmiSchedular.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_profiles")
@Getter
@Setter
public class FinancialProfile {

    @Id
    private String pan;
    private String name;
    private LocalDate dob;
    private BigDecimal monthlyIncome;

    private BigDecimal existingDebt;

    private Integer creditScore;
}
