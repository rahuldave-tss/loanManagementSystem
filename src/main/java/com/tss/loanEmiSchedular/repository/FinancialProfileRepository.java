package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile,String> {
}
