package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.response.PaymentHistoryResponseDto;
import com.tss.loanEmiSchedular.dto.response.PaymentResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentService {

    @Transactional
    PaymentResponseDto payEmi(String email, Long loanId);

    // ── Payment history for an EMI ────────────────────────────────────────────
    List<PaymentHistoryResponseDto> getPaymentHistoryByEmi(String email, Long emiId);

    List<PaymentHistoryResponseDto> getPaymentHistory(String email, Long loanId);
}
