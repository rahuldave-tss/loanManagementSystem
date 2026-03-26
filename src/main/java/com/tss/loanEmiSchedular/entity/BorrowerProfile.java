package com.tss.loanEmiSchedular.entity;

import jakarta.persistence.*;
import jdk.jfr.StackTrace;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "borrower_profiles")
@Getter
@Setter
public class BorrowerProfile extends BaseEntity{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;
    @Column(unique = true,nullable = false)
    private String pan;
    @Column(unique = true)
    private String aadhaar;

    @OneToOne(mappedBy = "borrower",cascade = CascadeType.ALL)
    private Address address;
}
