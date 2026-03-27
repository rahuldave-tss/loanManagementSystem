package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.strategy.LoanStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LowRiskStrategy implements LoanStrategy {

    @Override
    public LoanStrategyType decide(BigDecimal dti, int tenure) {
        return LoanStrategyType.FLAT;
    }
}
