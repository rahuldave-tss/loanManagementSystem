package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.events.*;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleLoanApplied(LoanAppliedEvent loanAppliedEvent){
        notificationService.sendLoanApplicationEmail(loanAppliedEvent.getEmail(),
                loanAppliedEvent.getLoan());
    }

    @Async
    @EventListener
    public void handleLoanDecision(LoanDecisionEvent loanDecisionEvent){
        notificationService.sendLoanDecisionEmail(loanDecisionEvent.getEmail(),
                loanDecisionEvent.getLoan());
    }

    @Async
    @EventListener
    public void handlePaymentReminder(PaymentReminderEvent event){
        notificationService.sendPaymentReminder(
                event.getEmi().getLoan().getBorrower().getUser().getEmail(),
                event.getEmi()
        );
    }

    @Async
    @EventListener
    public void handleOverdue(EmiOverdueEvent event) {
        notificationService.sendOverdueAlert(
                event.getEmi().getLoan().getBorrower().getUser().getEmail(),
                event.getEmi()
        );
    }

    @Async
    @EventListener
    public void handleSignup(SignupEvent event)
    {
        notificationService.sendOTP(event.getEmail(),event.getOtp());
    }

}
