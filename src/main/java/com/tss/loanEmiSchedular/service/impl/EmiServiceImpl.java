package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.service.EmiService;
import com.tss.loanEmiSchedular.strategy.EmiStrategy;
import com.tss.loanEmiSchedular.strategy.EmiStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmiServiceImpl implements EmiService {
    private final EmiStrategyFactory emiStrategyFactory;
    private final EmiRepository emiRepository;

    @Override
    public void generateSchedule(Loan loan) {
        EmiStrategy strategy= emiStrategyFactory.getStrategy(loan.getSelectedStrategy());

        List<Emi> emis=strategy.generateSchedule(loan);

        emiRepository.saveAll(emis);

    }
}
