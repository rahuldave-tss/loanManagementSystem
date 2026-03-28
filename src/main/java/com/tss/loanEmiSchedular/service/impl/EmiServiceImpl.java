package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.EmiStatus;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.service.EmiService;
import com.tss.loanEmiSchedular.strategy.EmiStrategy;
import com.tss.loanEmiSchedular.strategy.EmiStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmiServiceImpl implements EmiService {
    private final EmiStrategyFactory emiStrategyFactory;
    private final EmiRepository emiRepository;

    @Value("${emi.penalty.rate}")
    private BigDecimal penaltyRate;



    @Override
    public void generateSchedule(Loan loan) {
        EmiStrategy strategy= emiStrategyFactory.getStrategy(loan.getSelectedStrategy());

        List<Emi> emis=strategy.generateSchedule(loan);

        emiRepository.saveAll(emis);

    }

    @Override
    public void markOverdueEmis() {
        List<Emi> emis=emiRepository.findAllPendingEmis();

        LocalDate today=LocalDate.now();

        for(Emi emi:emis){

            if (!emi.isFullyPaid() &&
                    emi.getEmiStatus() == EmiStatus.PENDING &&
                    emi.getDueDate().isBefore(today)) {

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
            }
        }

        emiRepository.saveAll(emis);
    }
}
