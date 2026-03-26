package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.EmiStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "emis")
@Getter
@Setter
public class Emi extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;

    private Integer installmentNumber;

    private Double principal;
    private Double interest;

    private Double penaltyAmount;
    private Double totalPaidAmount;

    private Boolean isFullyPaid=false;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private EmiStatus emiStatus;
}
