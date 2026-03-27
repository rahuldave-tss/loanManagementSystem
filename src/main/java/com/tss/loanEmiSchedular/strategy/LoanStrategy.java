package com.tss.loanEmiSchedular.strategy;

import com.tss.loanEmiSchedular.enums.LoanStrategyType;

import java.math.BigDecimal;

public interface LoanStrategy {
    LoanStrategyType decide(BigDecimal dti, int tenure);

}
