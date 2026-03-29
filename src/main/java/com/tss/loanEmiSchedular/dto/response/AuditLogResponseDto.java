package com.tss.loanEmiSchedular.dto.response;

import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.ActorType;
import com.tss.loanEmiSchedular.enums.AuditAction;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLogResponseDto {
    private Long id;
    private Long loanId;
    private Long emiId;
    public String performedBy;
    public ActorType actorType;
    public AuditAction auditAction;
    public LoanStrategyType prevStrategy;
    public LoanStrategyType newStrategy;
    public LoanStatus prevStatus;
    public LoanStatus newStatus;
    public String remarks;
    private LocalDateTime timestamp;
}
