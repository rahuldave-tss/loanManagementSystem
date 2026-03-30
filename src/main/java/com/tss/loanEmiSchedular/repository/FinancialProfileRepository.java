package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, String> {
    Optional<FinancialProfile> findByPanAndNameAndDob(String pan, String name, LocalDate dob);

}
