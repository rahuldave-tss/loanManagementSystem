package com.tss.loanEmiSchedular.entity;

import com.tss.loanEmiSchedular.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
public class User extends BaseEntity{


    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private BorrowerProfile borrowerProfile;

    private Boolean isActive=true;
    private LocalDateTime lastLogin;
    private Boolean isKycVerified=false;
}
