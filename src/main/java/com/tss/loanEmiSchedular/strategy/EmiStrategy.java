package com.tss.loanEmiSchedular.strategy;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;

import java.math.BigDecimal;
import java.util.List;

public interface EmiStrategy {
    List<Emi> generateSchedule(Loan loan);
    BigDecimal calculateEmi(Loan loan);
}
