package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.response.BorrowerLoanResponseDto;
import com.tss.loanEmiSchedular.dto.response.EmiResponseDto;

import java.util.List;

public interface BorrowerService {
    // ── 1. All loans ─────────────────────────────────────────────────────────
    List<BorrowerLoanResponseDto> getMyLoans(String email);

    // ── 2. All EMIs for a loan ────────────────────────────────────────────────
    List<EmiResponseDto> getEmisForLoan(String email, Long loanId);
}
