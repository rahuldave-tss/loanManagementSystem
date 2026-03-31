package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.EmiStatus;
import com.tss.loanEmiSchedular.events.EmiOverdueEvent;
import com.tss.loanEmiSchedular.events.PaymentReminderEvent;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.service.EmiService;
import com.tss.loanEmiSchedular.strategy.EmiStrategy;
import com.tss.loanEmiSchedular.strategy.EmiStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmiServiceImpl implements EmiService {
    private final EmiStrategyFactory emiStrategyFactory;
    private final EmiRepository emiRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${emi.penalty.rate}")
    private BigDecimal penaltyRate;


    @Override
    public void generateSchedule(Loan loan) {
        EmiStrategy strategy= emiStrategyFactory.getStrategy(loan.getSelectedStrategy());

        List<Emi> emis=strategy.generateSchedule(loan);

        emiRepository.saveAll(emis);

    }

    @Transactional
    @Override
    public void markOverdueEmis() {
        log.info("EMI overdue schedular started");
        int count=0;

        List<Emi> emis=emiRepository.findAllPendingEmis();

        LocalDate today=LocalDate.now();

        for(Emi emi:emis){
            sendPaymentReminder(emi);

            if (!emi.isFullyPaid() &&
                    emi.getEmiStatus() == EmiStatus.PENDING &&
                    emi.getDueDate().isBefore(today)) {

                count++;

                emi.setEmiStatus(EmiStatus.OVERDUE);


                //add penalty - 2% of remaining amount
                //then total due amount increases and borrower has to pay totalDueAmount
                BigDecimal penalty=emi.getRemainingAmount()
                        .multiply(penaltyRate);

                emi.setPenaltyAmount(penalty);

                BigDecimal currentDue = emi.getTotalDueAmount() != null
                        ? emi.getTotalDueAmount()
                        : emi.getRemainingAmount();

                emi.setTotalDueAmount(currentDue.add(penalty));

                applicationEventPublisher.publishEvent(new EmiOverdueEvent(emi));
            }
        }

        emiRepository.saveAll(emis);
        log.info("Marked {} EMIs as overdue",count);
    }

    @Override
    public BigDecimal calculateBaseEmi(Loan loan) {
        EmiStrategy strategy= emiStrategyFactory.getStrategy(loan.getSuggestedStrategy());

        return strategy.calculateEmi(loan);
    }

    @Override
    public void sendPaymentReminder(Emi emi) {
        LocalDate today=LocalDate.now();

        long daysLeft = ChronoUnit.DAYS.between(today, emi.getDueDate());

        if(!emi.isFullyPaid() &&
                emi.getEmiStatus() == EmiStatus.PENDING &&
                daysLeft>=0 &&
                daysLeft<=3){

            applicationEventPublisher.publishEvent(new PaymentReminderEvent(emi));
            log.info("Pending Amount Reminder sent to : "+emi.getLoan().getBorrower().getUser().getEmail());
        }
    }


}
