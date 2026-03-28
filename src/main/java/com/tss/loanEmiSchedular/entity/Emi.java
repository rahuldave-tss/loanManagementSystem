package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.EmiStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "emis")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Emi extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;

    private Integer installmentNumber;

    private BigDecimal principal;
    private BigDecimal interest;

    private BigDecimal penaltyAmount;
    private BigDecimal remainingAmount;
    private BigDecimal totalPaidAmount;

    private BigDecimal totalDueAmount;

    private boolean isFullyPaid;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private EmiStatus emiStatus;
}
