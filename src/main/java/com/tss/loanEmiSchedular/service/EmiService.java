package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.entity.Loan;

public interface EmiService {
    void generateSchedule(Loan loan);
    void markOverdueEmis();
}
