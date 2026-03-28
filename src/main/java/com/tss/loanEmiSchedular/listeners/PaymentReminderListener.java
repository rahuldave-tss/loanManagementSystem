package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.events.PaymentReminderEvent;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReminderListener {
    private final NotificationService notificationService;

    @EventListener
    public void handlePaymentReminder(PaymentReminderEvent event){
        notificationService.sendPaymentReminder(
                event.getEmi().getLoan().getBorrower().getUser().getEmail(),
                event.getEmi()
        );
    }
}
