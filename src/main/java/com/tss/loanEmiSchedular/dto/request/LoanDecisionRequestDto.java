package com.tss.loanEmiSchedular.dto.request;

import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanDecisionRequestDto {
    @NotNull(message = "Decision is required")
    private LoanStatus decision;
    private LoanStrategyType overrideStrategy;
}
