package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmiRepository extends JpaRepository<Emi, Long> {
    @Query("""
        SELECT e FROM Emi e
        JOIN FETCH e.loan l
        JOIN FETCH l.borrower b
        JOIN FETCH b.user u
        WHERE e.emiStatus='PENDING'
        AND e.dueDate < CURRENT_DATE
    """)
    List<Emi> findAllPendingEmis();

    @Query("""
    SELECT e FROM Emi e
    JOIN FETCH e.loan l
    JOIN FETCH l.borrower b
    JOIN FETCH b.user u
    WHERE e.emiStatus = 'PENDING'
    AND e.dueDate BETWEEN :startDate AND :endDate
""")
    List<Emi> findUpcomingEmis(LocalDate startDate, LocalDate endDate);


    List<Emi> findByLoanIdOrderByInstallmentNumberAsc(Long loanId);

    @Query("""
    SELECT e FROM Emi e
    WHERE e.loan.id = :loanId
    AND e.isFullyPaid = false
    AND e.emiStatus IN ('PENDING','OVERDUE')
    ORDER BY e.installmentNumber ASC
""")
    List<Emi> findUnpaidEmisByLoanIdOrdered(@Param("loanId") Long loanId);



    Optional<Emi> findById(Long emiId);
    

    boolean existsByLoanId(Long loanId);
}
