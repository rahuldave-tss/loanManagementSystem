package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loan extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id",nullable = false)
    private BorrowerProfile borrower;

    private Double loanAmount;
    private Integer tenure;

    //snapshot from financialProfile
    private Double monthlyIncome;
    private Double existingDebt;
    private Double dti;
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    private LoanStrategyType suggestedStrategy;
    @Enumerated(EnumType.STRING)
    private LoanStrategyType selectedStrategy;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private Boolean isDeleted=false;
}
