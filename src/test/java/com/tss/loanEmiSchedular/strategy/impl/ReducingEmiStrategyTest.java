package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReducingEmiStrategyTest {

    private ReducingEmiStrategy strategy;

    @BeforeEach
    void setUp(){
        strategy=new ReducingEmiStrategy();
    }

    @Test
    void shouldGenerateReducingEmiSchedule() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(100000));
        loan.setInterestRate(BigDecimal.valueOf(12)); // 12%
        loan.setTenure(12);
        loan.setCreatedAt(LocalDate.of(2024, 1, 10).atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        assertEquals(12, emis.size());

        BigDecimal emiAmount = emis.get(0).getTotalDueAmount();

        for (Emi emi : emis) {
            // EMI should be constant
            assertEquals(0, emiAmount.compareTo(emi.getTotalDueAmount()));
        }
    }

    @Test
    void interestShouldDecreaseOverTime() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(100000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(12);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        for (int i = 1; i < emis.size(); i++) {
            BigDecimal prevInterest = emis.get(i - 1).getInterest();
            BigDecimal currInterest = emis.get(i).getInterest();

            assertTrue(currInterest.compareTo(prevInterest) < 0);
        }
    }

    @Test
    void principalShouldIncreaseOverTime() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(100000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(12);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        for (int i = 1; i < emis.size(); i++) {
            BigDecimal prevPrincipal = emis.get(i - 1).getPrincipal();
            BigDecimal currPrincipal = emis.get(i).getPrincipal();

            assertTrue(currPrincipal.compareTo(prevPrincipal) > 0);
        }
    }

    @Test
    void lastEmiShouldAdjustRemainingPrincipal() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(10000));
        loan.setInterestRate(BigDecimal.valueOf(10));
        loan.setTenure(3);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        // Sum of all principal should match loan amount
        BigDecimal totalPrincipal = emis.stream()
                .map(Emi::getPrincipal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(0, totalPrincipal.compareTo(loan.getLoanAmount()));
    }

    @Test
    void remainingPrincipalShouldBeZeroAtEnd() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(50000));
        loan.setInterestRate(BigDecimal.valueOf(10));
        loan.setTenure(10);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        BigDecimal totalPrincipalPaid = BigDecimal.ZERO;

        for (Emi emi : emis) {
            totalPrincipalPaid = totalPrincipalPaid.add(emi.getPrincipal());
        }

        assertEquals(0, totalPrincipalPaid.compareTo(loan.getLoanAmount()));
    }

}