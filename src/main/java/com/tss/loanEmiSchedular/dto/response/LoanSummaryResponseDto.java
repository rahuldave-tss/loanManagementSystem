package com.tss.loanEmiSchedular.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanSummaryResponseDto {
    private Long loanId;
    private String borrowerEmail;
    private BigDecimal loanAmount;
    private Integer tenure;
    private BigDecimal dti;
    private String suggestedStrategy;
    private Integer creditScore;
    private String status;
}
