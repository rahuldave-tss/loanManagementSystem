package com.tss.loanEmiSchedular.schedular;

import com.tss.loanEmiSchedular.service.EmiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmiSchedular {
    private final EmiService emiService;

    //minute&second hour everyDay everyMonth anyDayOfTheWeek
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
    public void markOverdueEmis(){
        emiService.markOverdueEmis();
        emiService.sendPaymentReminder();
    }
}
