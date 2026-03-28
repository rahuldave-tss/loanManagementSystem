package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.events.LoanAppliedEvent;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleLoanApplied(LoanAppliedEvent loanAppliedEvent){
        notificationService.sendLoanApplicationEmail(loanAppliedEvent.getEmail(),
                loanAppliedEvent.getLoan());
    }
}
