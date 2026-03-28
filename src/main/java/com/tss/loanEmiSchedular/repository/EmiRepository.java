package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.Emi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmiRepository extends JpaRepository<Emi,Integer> {
    @Query("""
        SELECT e FROM Emi e
        WHERE e.emiStatus='PENDING'
        AND e.dueDate < CURRENT_DATE
    """)
    List<Emi> findAllPendingEmis();
}
