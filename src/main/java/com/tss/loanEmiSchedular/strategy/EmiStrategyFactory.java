package com.tss.loanEmiSchedular.strategy;

import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmiStrategyFactory {

    private final Map<String, EmiStrategy> strategyMap;

    public EmiStrategy getStrategy(LoanStrategyType type) {

        EmiStrategy strategy = strategyMap.get(type.name());

        if (strategy == null) {
            throw new RuntimeException("No EMI strategy found for: " + type);
        }

        return strategy;
    }
}
