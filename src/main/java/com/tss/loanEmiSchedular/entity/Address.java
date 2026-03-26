package com.tss.loanEmiSchedular.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address extends BaseEntity{

    private String street;
    private String city;
    private String state;
    private String pincode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id",nullable = false,unique = true)
    private BorrowerProfile borrower;
}
