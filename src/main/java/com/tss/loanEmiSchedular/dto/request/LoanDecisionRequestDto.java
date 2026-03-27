package com.tss.loanEmiSchedular.dto.request;

import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanDecisionRequestDto {
    private LoanStatus decision;
    private LoanStrategyType overrideStrategy;
}
