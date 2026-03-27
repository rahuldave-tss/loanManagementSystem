package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.strategy.LoanStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MidRiskStrategy implements LoanStrategy {

    @Override
    public LoanStrategyType decide(BigDecimal dti, int tenure) {

        if (tenure < 24) {
            return LoanStrategyType.REDUCING;
        } else {
            return LoanStrategyType.STEP_UP;
        }
    }
}