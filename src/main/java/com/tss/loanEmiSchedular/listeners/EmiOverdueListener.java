package com.tss.loanEmiSchedular.listeners;

import com.tss.loanEmiSchedular.events.EmiOverdueEvent;
import com.tss.loanEmiSchedular.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmiOverdueListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleOverdue(EmiOverdueEvent event) {
        notificationService.sendOverdueAlert(
                event.getEmi().getLoan().getBorrower().getUser().getEmail(),
                event.getEmi()
        );
    }
}
