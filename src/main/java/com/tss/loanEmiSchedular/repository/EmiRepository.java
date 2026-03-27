package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.Emi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmiRepository extends JpaRepository<Emi,Integer> {
}
