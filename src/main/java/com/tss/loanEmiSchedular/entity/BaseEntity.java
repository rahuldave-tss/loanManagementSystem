package com.tss.loanEmiSchedular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }
}
