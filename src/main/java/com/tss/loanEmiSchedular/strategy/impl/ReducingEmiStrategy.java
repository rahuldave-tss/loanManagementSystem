package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.EmiStatus;
import com.tss.loanEmiSchedular.strategy.EmiStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("REDUCING")
public class ReducingEmiStrategy implements EmiStrategy {

    @Override
    @Transactional
    public List<Emi> generateSchedule(Loan loan) {
        List<Emi> emis=new ArrayList<>();

        BigDecimal principal=loan.getLoanAmount();
        BigDecimal monthlyRate=loan.getInterestRate()
                .divide(BigDecimal.valueOf(12*100),10, RoundingMode.HALF_UP);

        int tenure= loan.getTenure();

        double emiAmountDouble=(principal.doubleValue() * monthlyRate.doubleValue() *
                Math.pow(1 + monthlyRate.doubleValue(), tenure)) /
                (Math.pow(1 + monthlyRate.doubleValue(), tenure) - 1);

        BigDecimal emiAmount = BigDecimal.valueOf(emiAmountDouble)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal remainingPrincipal = principal;

        for (int i = 1; i <= tenure; i++) {
            LocalDate startDate = loan.getCreatedAt().toLocalDate();
            int emiDay = startDate.getDayOfMonth();

            LocalDate dueDate = startDate.plusMonths(i);
            int lastDay = dueDate.lengthOfMonth();

            dueDate = dueDate.withDayOfMonth(Math.min(emiDay, lastDay));

            BigDecimal interest = remainingPrincipal.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalPart = emiAmount.subtract(interest)
                    .setScale(2, RoundingMode.HALF_UP);

            if (i == tenure) {
                principalPart = remainingPrincipal; // last EMI correction
            }

            remainingPrincipal = remainingPrincipal.subtract(principalPart);

            Emi emi = Emi.builder()
                    .loan(loan)
                    .installmentNumber(i)
                    .dueDate(dueDate)
                    .principal(principalPart)
                    .interest(interest)
                    .remainingAmount(emiAmount)
                    .penaltyAmount(BigDecimal.ZERO)
                    .totalPaidAmount(BigDecimal.ZERO)
                    .isFullyPaid(false)
                    .emiStatus(EmiStatus.PENDING)
                    .totalDueAmount(emiAmount)
                    .build();

            emis.add(emi);
        }

        return emis;
    }

    @Override
    public BigDecimal calculateEmi(Loan loan) {
        BigDecimal principal=loan.getLoanAmount();
        BigDecimal monthlyRate=loan.getInterestRate()
                .divide(BigDecimal.valueOf(12*100),10, RoundingMode.HALF_UP);

        int tenure= loan.getTenure();

        double emiAmountDouble=(principal.doubleValue() * monthlyRate.doubleValue() *
                Math.pow(1 + monthlyRate.doubleValue(), tenure)) /
                (Math.pow(1 + monthlyRate.doubleValue(), tenure) - 1);

        BigDecimal emiAmount = BigDecimal.valueOf(emiAmountDouble)
                .setScale(2, RoundingMode.HALF_UP);

        return emiAmount;
    }
}
