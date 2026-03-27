package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.enums.LoanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loan extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id",nullable = false)
    private BorrowerProfile borrower;

    private BigDecimal loanAmount;
    private Integer tenure;
    private BigDecimal interestRate;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    //snapshot from financialProfile
    private BigDecimal monthlyIncome;
    private BigDecimal remainingBalance;
    private BigDecimal dti;
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    private LoanStrategyType suggestedStrategy;
    @Enumerated(EnumType.STRING)
    private LoanStrategyType selectedStrategy;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private boolean isDeleted;
}
