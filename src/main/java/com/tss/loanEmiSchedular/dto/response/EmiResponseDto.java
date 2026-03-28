package com.tss.loanEmiSchedular.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EmiResponseDto {
    private Long emiId;
    private Integer installmentNumber;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal totalDueAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal penaltyAmount;
    private boolean isFullyPaid;
    private LocalDate dueDate;
    private String emiStatus;
}