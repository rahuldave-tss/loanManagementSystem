package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emi_id",nullable = false)
    private Emi emi;

    private Double amount;

    private PaymentStatus paymentStatus;

    private String transactionId;

}
