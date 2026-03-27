package com.tss.loanEmiSchedular.strategy;

import com.tss.loanEmiSchedular.strategy.impl.HighRiskStrategy;
import com.tss.loanEmiSchedular.strategy.impl.LowRiskStrategy;
import com.tss.loanEmiSchedular.strategy.impl.MidRiskStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LoanStrategyFactory {

    private final LowRiskStrategy lowRiskStrategy;
    private final MidRiskStrategy midRiskStrategy;
    private final HighRiskStrategy highRiskStrategy;

    public LoanStrategy getStrategy(BigDecimal dti) {

        if (dti.compareTo(BigDecimal.valueOf(20)) < 0) {
            return lowRiskStrategy;
        } else if (dti.compareTo(BigDecimal.valueOf(40)) <= 0) {
            return midRiskStrategy;
        } else {
            return highRiskStrategy;
        }
    }
}