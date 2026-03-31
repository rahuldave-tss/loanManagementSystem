package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StepUpEmiStrategyTest {

    private StepUpEmiStrategy strategy;

    @BeforeEach
    void setUp(){
        strategy=new StepUpEmiStrategy();
    }

    @Test
    void emiShouldIncreaseAfterStepPoint() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(100000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(10);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        int stepPoint = loan.getTenure() / 2;

        BigDecimal baseEmi = emis.get(0).getTotalDueAmount();
        BigDecimal increasedEmi = emis.get(stepPoint).getTotalDueAmount();

        // EMI after step should be greater
        assertTrue(increasedEmi.compareTo(baseEmi) > 0);
    }

    @Test
    void firstHalfEmiShouldBeConstant() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(100000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(10);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        BigDecimal firstEmi = emis.get(0).getTotalDueAmount();

        for (int i = 0; i < loan.getTenure() / 2; i++) {
            assertEquals(0, firstEmi.compareTo(emis.get(i).getTotalDueAmount()));
        }
    }

    @Test
    void secondHalfEmiShouldBeIncreasedByTenPercent() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(100000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(10);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        int stepPoint = loan.getTenure() / 2;

        BigDecimal baseEmi = emis.get(0).getTotalDueAmount();

        BigDecimal expectedIncreased = baseEmi
                .multiply(BigDecimal.valueOf(1.10))
                .setScale(2, RoundingMode.HALF_UP);

        for (int i = stepPoint; i < loan.getTenure() - 1; i++) {
            assertEquals(0, expectedIncreased.compareTo(emis.get(i).getTotalDueAmount()));
        }
    }

}