package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;

import java.math.BigDecimal;

public interface EmiService {
    void generateSchedule(Loan loan);
    void markOverdueEmis();
    BigDecimal calculateBaseEmi(Loan loan);
    void sendPaymentReminder(Emi emi);
}
