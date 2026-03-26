package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.AuditAction;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id",nullable = false)
    private User officer;

    @Enumerated(EnumType.STRING)
    private AuditAction auditAction;

    private String remarks;
    @Enumerated(EnumType.STRING)
    private LoanStrategyType previousStrategy;
    @Enumerated(EnumType.STRING)
    private LoanStrategyType newStrategy;

}
