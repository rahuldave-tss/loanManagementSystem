package com.tss.loanEmiSchedular.strategy;

import com.tss.loanEmiSchedular.strategy.impl.HighRiskStrategy;
import com.tss.loanEmiSchedular.strategy.impl.LowRiskStrategy;
import com.tss.loanEmiSchedular.strategy.impl.MidRiskStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.tss.loanEmiSchedular.util.AppConstants.HIGH_DTI_THRESHOLD;
import static com.tss.loanEmiSchedular.util.AppConstants.LOW_DTI_THRESHOLD;

@Service
@RequiredArgsConstructor
public class LoanStrategyFactory {

    private final LowRiskStrategy lowRiskStrategy;
    private final MidRiskStrategy midRiskStrategy;
    private final HighRiskStrategy highRiskStrategy;

    public LoanStrategy getStrategy(BigDecimal dti) {

        if (dti.compareTo(BigDecimal.valueOf(LOW_DTI_THRESHOLD)) < 0) {
            return lowRiskStrategy;
        } else if (dti.compareTo(BigDecimal.valueOf(HIGH_DTI_THRESHOLD)) <= 0) {
            return midRiskStrategy;
        } else {
            return highRiskStrategy;
        }
    }
}