package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.events.*;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;
    private final EmiRepository emiRepository;

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
    @Transactional
    public void handlePaymentReminder(PaymentReminderEvent event){

        Emi emi=emiRepository.findById(event.getEmiId())
                        .orElseThrow(()->new RuntimeException("Emi not found"));

        notificationService.sendPaymentReminder(
                event.getEmail(),
                emi
        );
    }

    @Async
    @Transactional
    @EventListener
    public void handleOverdue(EmiOverdueEvent event) {

        Emi emi=emiRepository.findById(event.getEmiId())
                .orElseThrow(()->new RuntimeException("Emi not found"));

        notificationService.sendOverdueAlert(
                emi.getLoan().getBorrower().getUser().getEmail(),
                emi
        );
    }

    @Async
    @EventListener
    public void handleSignup(SignupEvent event)
    {
        notificationService.sendOTP(event.getEmail(),event.getOtp());
    }

}
