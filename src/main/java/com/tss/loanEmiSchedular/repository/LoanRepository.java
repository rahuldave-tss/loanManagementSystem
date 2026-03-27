package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBorrowerAndStatus(BorrowerProfile borrower, LoanStatus status);
}