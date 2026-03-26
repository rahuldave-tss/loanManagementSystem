package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emi_id",nullable = false)
    private Emi emi;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private String transactionId;

}
