package com.tss.loanEmiSchedular.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentHistoryResponseDto {
    private Long paymentId;
    private Long loanId;
    private Long emiId;
    private Integer installmentNumber;
    private BigDecimal amount;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime paidAt;
}