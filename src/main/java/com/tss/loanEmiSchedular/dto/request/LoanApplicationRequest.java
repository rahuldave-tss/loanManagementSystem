package com.tss.loanEmiSchedular.dto.request;

import com.tss.loanEmiSchedular.enums.LoanType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequest {
    private BigDecimal loanAmount;
    private Integer tenure;
    private LoanType loanType;
}
