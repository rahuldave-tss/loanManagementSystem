package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.EmiStatus;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlatEmiStrategyTest {

    private FlatEmiStrategy strategy;

    @BeforeEach
    void setUp(){
        strategy=new FlatEmiStrategy();
    }

    //Arrange -> Act -> Assert

    @Test
    void shouldGenerateCorrectEmiSchedule(){

        //Arrange
        Loan loan=new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(12000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(12);
        loan.setCreatedAt(LocalDate.of(2024,1,10).atStartOfDay());

        //Act
        List<Emi> emis=strategy.generateSchedule(loan);

        //Assert
        assertEquals(12,emis.size());


        Emi firstEmi=emis.get(0);
        // EMI = (12000 + interest) / 12
        // interest = 12000 * 12 * 12 / 1200 = 1440
        // total = 13440 → EMI = 1120

        assertEquals(BigDecimal.valueOf(1120).setScale(2), firstEmi.getTotalDueAmount());
        assertEquals(BigDecimal.valueOf(1000).setScale(2), firstEmi.getPrincipal());
        assertEquals(BigDecimal.valueOf(120).setScale(2), firstEmi.getInterest());

        assertEquals(EmiStatus.PENDING, firstEmi.getEmiStatus());
        assertFalse(firstEmi.isFullyPaid());


    }

    @Test
    void shouldHandleMonthEndDatesCorrectly() {
        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(1000));
        loan.setInterestRate(BigDecimal.valueOf(10));
        loan.setTenure(3);

        // Jan 31 → Feb should become Feb 29/28
        loan.setCreatedAt(LocalDate.of(2024, 1, 31).atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        assertEquals(LocalDate.of(2024, 2, 29), emis.get(0).getDueDate()); // leap year
        assertEquals(LocalDate.of(2024, 3, 31), emis.get(1).getDueDate());

    }

    @Test
    void allEmisShouldHaveSameAmounts() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(6000));
        loan.setInterestRate(BigDecimal.valueOf(12));
        loan.setTenure(6);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        BigDecimal firstEmiAmount = emis.get(0).getTotalDueAmount();

        for (Emi emi : emis) {
            assertEquals(firstEmiAmount, emi.getTotalDueAmount());
            assertEquals(BigDecimal.ZERO, emi.getPenaltyAmount());
            assertEquals(BigDecimal.ZERO, emi.getTotalPaidAmount());
        }
    }

    @Test
    void shouldHandleSingleEmiCase() {

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(1000));
        loan.setInterestRate(BigDecimal.valueOf(10));
        loan.setTenure(1);
        loan.setCreatedAt(LocalDate.now().atStartOfDay());

        List<Emi> emis = strategy.generateSchedule(loan);

        assertEquals(1, emis.size());

        Emi emi = emis.get(0);

        assertEquals(emi.getTotalDueAmount(), emi.getRemainingAmount());
    }

}