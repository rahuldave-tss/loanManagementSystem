package com.tss.loanEmiSchedular.dto.request;

import com.tss.loanEmiSchedular.enums.LoanType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequest {
    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1000.00", message = "Loan amount must be at least 1000")
    @DecimalMax(value = "10000000.00", message = "Loan amount cannot exceed 1 crore")
    private BigDecimal loanAmount;

    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    @Max(value = 360, message = "Tenure cannot exceed 360 months")
    private Integer tenure;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;
}
