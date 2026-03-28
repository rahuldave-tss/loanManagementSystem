package com.tss.loanEmiSchedular.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponseDto {
    private String transactionId;
    private String loanId;
    private Long emiId;
    private Integer installmentNumber;
    private BigDecimal amountPaid;
    private String paymentStatus;
    private String message;
}