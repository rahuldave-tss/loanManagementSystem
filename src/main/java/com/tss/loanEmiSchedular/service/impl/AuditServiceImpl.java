package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.response.AuditLogResponseDto;
import com.tss.loanEmiSchedular.entity.AuditLog;
import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.ActorType;
import com.tss.loanEmiSchedular.enums.AuditAction;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.mapper.AuditLogMapper;
import com.tss.loanEmiSchedular.repository.AuditLogRepository;
import com.tss.loanEmiSchedular.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void logLoanAction(Loan loan, User performedBy, ActorType actorType, AuditAction auditAction, LoanStatus prevStatus, LoanStatus newStatus, String remarks) {
        AuditLog log=new AuditLog();
        log.setLoan(loan);
        log.setPerformedBy(performedBy);
        log.setActorType(actorType);
        log.setAuditAction(auditAction);
        log.setPrevStatus(prevStatus);
        log.setNewStatus(newStatus);
        log.setRemarks(remarks);

        auditLogRepository.save(log);
    }

    @Override
    public void logEmiAction(Loan loan, Emi emi, ActorType actorType, AuditAction auditAction, String remarks) {
        AuditLog log=new AuditLog();
        log.setLoan(loan);
        log.setEmi(emi);
        log.setActorType(actorType);
        log.setAuditAction(auditAction);
        log.setRemarks(remarks);

        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLogResponseDto> viewAuditLogsOfLoan(Long loanId) {
        List<AuditLog> auditLogs=auditLogRepository.getAuditLogsOfLoan(loanId);

        if(auditLogs.isEmpty()){
            throw new RuntimeException("No Audit Logs Found");
        }

        return auditLogs.stream().map(auditLogMapper::toResponseDto).toList();
    }

    @Override
    public Page<AuditLogResponseDto> viewAuditLogsOfLoanPage(Long loanId, Pageable pageable) {
        Page<AuditLog> auditLogPage=auditLogRepository.getAuditLogsOfLoanPage(loanId,pageable);



        if (pageable.getPageNumber() >= auditLogPage.getTotalPages()) {
            throw new RuntimeException("Page number out of range");
        }
        return auditLogPage.map(auditLogMapper::toResponseDto);
    }
}
