package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.events.LoanAppliedEvent;
import com.tss.loanEmiSchedular.events.LoanDecisionEvent;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanDecisionListener {
    private final NotificationService notificationService;

    @EventListener
    public void handleLoanDecision(LoanDecisionEvent loanDecisionEvent){
        notificationService.sendLoanDecisionEmail(loanDecisionEvent.getEmail(),
                loanDecisionEvent.getLoan());
    }
}
