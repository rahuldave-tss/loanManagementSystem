package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.ActorType;
import com.tss.loanEmiSchedular.enums.AuditAction;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity{

    //which loan it belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;

    //who performed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officer_id")
    private User performedBy;

    //type of actor
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActorType actorType;

    //action
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction auditAction;

    //loan status change
    @Enumerated(EnumType.STRING)
    private LoanStatus prevStatus;
    @Enumerated(EnumType.STRING)
    private LoanStatus newStatus;

    //if emi changes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emi_id")
    private Emi emi;

    private String remarks;

    //only when strategy changes
    @Enumerated(EnumType.STRING)
    private LoanStrategyType previousStrategy;
    @Enumerated(EnumType.STRING)
    private LoanStrategyType newStrategy;

}
