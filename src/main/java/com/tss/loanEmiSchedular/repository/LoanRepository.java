package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBorrowerAndStatus(BorrowerProfile borrower, LoanStatus status);

    List<Loan> findByBorrowerId(Long borrowerId);

    List<Loan> findByBorrowerIdAndStatus(Long borrowerId, LoanStatus status);
    @Query("""
    SELECT l FROM Loan l
    JOIN FETCH l.borrower b
    JOIN FETCH b.user
    WHERE l.status = :status
""")
    List<Loan> findByStatusWithBorrower(LoanStatus status);

    @EntityGraph(attributePaths = {"borrower", "borrower.user"})
    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

    Page<Loan> findByBorrowerUserEmailAndIsDeletedFalse(String email, Pageable pageable);
    List<Loan> findByBorrowerUserEmailAndIsDeletedFalse(String email);

}