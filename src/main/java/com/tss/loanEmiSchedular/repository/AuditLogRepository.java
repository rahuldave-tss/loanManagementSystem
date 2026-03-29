package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {

    @Query("""
    SELECT a FROM AuditLog a
    LEFT JOIN FETCH a.performedBy
    LEFT JOIN FETCH a.emi
    WHERE a.loan.id = :loanId
""")
    List<AuditLog> getAuditLogsOfLoan(@Param("loanId") Long loanId);
}
