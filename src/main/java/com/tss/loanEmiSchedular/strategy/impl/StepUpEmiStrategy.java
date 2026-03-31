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

@Component("STEP_UP")
public class StepUpEmiStrategy implements EmiStrategy {

    private static final BigDecimal STEP_FACTOR = BigDecimal.valueOf(1.10);
    private static final int SCALE = 10;
    private static final BigDecimal TOLERANCE = BigDecimal.valueOf(0.01);

    @Override
    @Transactional
    public List<Emi> generateSchedule(Loan loan) {

        List<Emi> emis = new ArrayList<>();

        BigDecimal principal = loan.getLoanAmount();
        BigDecimal monthlyRate = getMonthlyRate(loan);

        int tenure = loan.getTenure();
        int stepPoint = tenure / 2;

        BigDecimal baseEmi = findBaseEmi(loan).setScale(2, RoundingMode.HALF_UP);

        BigDecimal remainingPrincipal = principal;

        for (int i = 1; i <= tenure; i++) {

            LocalDate startDate = loan.getCreatedAt().toLocalDate();
            int emiDay = startDate.getDayOfMonth();

            LocalDate dueDate = startDate.plusMonths(i);
            int lastDay = dueDate.lengthOfMonth();
            dueDate = dueDate.withDayOfMonth(Math.min(emiDay, lastDay));

            // Step-up logic
            BigDecimal emiAmount = (i <= stepPoint)
                    ? baseEmi
                    : baseEmi.multiply(STEP_FACTOR).setScale(2, RoundingMode.HALF_UP);

            BigDecimal interest = remainingPrincipal.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalPart = emiAmount.subtract(interest)
                    .setScale(2, RoundingMode.HALF_UP);

            // Last EMI correction
            if (i == tenure) {
                principalPart = remainingPrincipal;
                emiAmount = principalPart.add(interest).setScale(2, RoundingMode.HALF_UP);
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
        return findBaseEmi(loan).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal findBaseEmi(Loan loan) {

        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = loan.getLoanAmount().multiply(BigDecimal.valueOf(2));
        BigDecimal emi = BigDecimal.ZERO;

        for (int i = 0; i < 1000; i++) {

            emi = low.add(high).divide(BigDecimal.valueOf(2), SCALE, RoundingMode.HALF_UP);

            BigDecimal remaining = simulate(loan, emi);

            // Convergence check
//            if (remaining.abs().compareTo(TOLERANCE) < 0) {
//                break;
//            }

            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                low = emi;   // EMI too small
            } else {
                high = emi;  // EMI too big
            }
        }

        return emi;
    }

    private BigDecimal simulate(Loan loan, BigDecimal emi) {

        BigDecimal remaining = loan.getLoanAmount();
        BigDecimal monthlyRate = getMonthlyRate(loan);

        int tenure = loan.getTenure();
        int stepPoint = tenure / 2;

        for (int i = 1; i <= tenure; i++) {

            BigDecimal emiAmount = (i <= stepPoint)
                    ? emi
                    : emi.multiply(STEP_FACTOR).setScale(SCALE, RoundingMode.HALF_UP);

            BigDecimal interest = remaining.multiply(monthlyRate)
                    .setScale(SCALE, RoundingMode.HALF_UP);

            BigDecimal principal = emiAmount.subtract(interest)
                    .setScale(SCALE, RoundingMode.HALF_UP);

            remaining = remaining.subtract(principal)
                    .setScale(SCALE, RoundingMode.HALF_UP);
        }

        return remaining;
    }

    private BigDecimal getMonthlyRate(Loan loan) {
        return loan.getInterestRate()
                .divide(BigDecimal.valueOf(12 * 100), SCALE, RoundingMode.HALF_UP);
    }
}