package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.entity.User;

import java.math.BigDecimal;

public interface FinancialService {
    void addFirstEmiToExistingDebt(BigDecimal firstEmi, User user);
}
