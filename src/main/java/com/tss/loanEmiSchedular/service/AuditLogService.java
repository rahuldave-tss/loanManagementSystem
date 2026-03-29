package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.response.AuditLogResponseDto;
import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.ActorType;
import com.tss.loanEmiSchedular.enums.AuditAction;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuditLogService {
    void logLoanAction(Loan loan, User performedBy, ActorType actorType, AuditAction auditAction,
                       LoanStatus prevStatus,LoanStatus newStatus,String remarks);

    void logEmiAction(Loan loan, Emi emi, ActorType actorType,
                      AuditAction auditAction, String remarks);

    List<AuditLogResponseDto> viewAuditLogsOfLoan(Long loanId);
    Page<AuditLogResponseDto> viewAuditLogsOfLoanPage(Long loanId, Pageable pageable);
}
