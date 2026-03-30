package com.tss.loanEmiSchedular.strategy.impl;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.EmiStatus;
import com.tss.loanEmiSchedular.strategy.EmiStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("FLAT")
public class FlatEmiStrategy implements EmiStrategy {

    @Override
    public List<Emi> generateSchedule(Loan loan) {

        List<Emi> emis = new ArrayList<>();

        BigDecimal principal = loan.getLoanAmount();
        int tenure = loan.getTenure();

        BigDecimal totalInterest = principal
                .multiply(loan.getInterestRate())
                .multiply(BigDecimal.valueOf(tenure))
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = principal.add(totalInterest);

        BigDecimal emiAmount = totalAmount.divide(
                BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

        BigDecimal principalPart = principal.divide(
                BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

        BigDecimal interestPart = totalInterest.divide(
                BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

        for (int i = 1; i <= tenure; i++) {
            LocalDate startDate = loan.getCreatedAt().toLocalDate();
            int emiDay = startDate.getDayOfMonth();

            LocalDate dueDate = startDate.plusMonths(i);
            int lastDay = dueDate.lengthOfMonth();

            dueDate = dueDate.withDayOfMonth(Math.min(emiDay, lastDay));

            Emi emi = Emi.builder()
                    .loan(loan)
                    .installmentNumber(i)
                    .dueDate(dueDate)
                    .principal(principalPart)
                    .interest(interestPart)
                    //remaining emi amount - multiple payments
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
        BigDecimal principal = loan.getLoanAmount();
        int tenure = loan.getTenure();

        BigDecimal totalInterest = principal
                .multiply(loan.getInterestRate())
                .multiply(BigDecimal.valueOf(tenure))
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = principal.add(totalInterest);

        BigDecimal emiAmount = totalAmount.divide(
                BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

        return emiAmount;
    }
}
