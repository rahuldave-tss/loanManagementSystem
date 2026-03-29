package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.enums.ActorType;
import com.tss.loanEmiSchedular.enums.AuditAction;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.events.*;
import com.tss.loanEmiSchedular.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogService auditLogService;

    @Async
    @EventListener
    public void handleLoanApplied(LoanAppliedEvent event){
        auditLogService.logLoanAction(
                event.getLoan(),
                event.getUser(),
                ActorType.BORROWER,
                AuditAction.LOAN_APPLIED,
                null,
                LoanStatus.PENDING,
                "Loan applied by borrower"
        );
    }

    @Async
    @EventListener
    public void handleLoanDecision(LoanDecisionEvent event){

        LoanStatus prevStatus=LoanStatus.PENDING;
        LoanStatus newStatus=event.getLoan().getStatus();

        auditLogService.logLoanAction(
                event.getLoan(),
                event.getOfficer(),
                ActorType.OFFICER,
                newStatus==LoanStatus.ACTIVE?
                        AuditAction.LOAN_APPROVED:
                        AuditAction.LOAN_REJECTED,
                prevStatus,
                newStatus,
                "Loan Decision made by officer"
        );
    }

    @Async
    @EventListener
    public void handleReminder(PaymentReminderEvent event){
        auditLogService.logEmiAction(
                event.getEmi().getLoan(),
                event.getEmi(),
                ActorType.SYSTEM,
                AuditAction.PAYMENT_REMINDER_SENT,
                "Reminder sent 3 days before due date"
        );
    }

    @Async
    @EventListener
    public void handleOverdue(EmiOverdueEvent event){
        auditLogService.logEmiAction(
                event.getEmi().getLoan(),
                event.getEmi(),
                ActorType.SYSTEM,
                AuditAction.EMI_MARKED_OVERDUE,
                "EMI marked as overdue"
        );
    }

    @Async
    @EventListener
    public void handleEmiPaid(EmiPaidEvent event){
        auditLogService.logEmiAction(
                event.getEmi().getLoan(),
                event.getEmi(),
                ActorType.BORROWER,
                AuditAction.EMI_PAYMENT_DONE,
                "EMI paid by borrower"
        );
    }

}

