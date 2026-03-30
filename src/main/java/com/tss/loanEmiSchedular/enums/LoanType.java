package com.tss.loanEmiSchedular.enums;

import java.math.BigDecimal;

public enum LoanType {

    PERSONAL(12.0),
    HOME(8.0),
    CAR(9.0),
    EDUCATION(7.0);

    private final double interestRate;

    LoanType(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }
}