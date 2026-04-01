package com.tss.loanEmiSchedular.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BorrowerLoanResponseDto {
    private Long loanId;
    private BigDecimal loanAmount;
    private Integer tenure;
    private BigDecimal interestRate;
    private String loanType;
    private String status;
    private Integer creditScore;
}