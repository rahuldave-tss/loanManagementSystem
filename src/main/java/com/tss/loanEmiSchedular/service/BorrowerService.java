package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.response.BorrowerLoanResponseDto;
import com.tss.loanEmiSchedular.dto.response.EmiResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BorrowerService {
    // ── 1. All loans ─────────────────────────────────────────────────────────
    Page<BorrowerLoanResponseDto> getMyLoansByPage(String email, Pageable pageable);

    List<BorrowerLoanResponseDto> getMyLoans(String email);

    // ── 2. All EMIs for a loan ────────────────────────────────────────────────
    List<EmiResponseDto> getEmisForLoan(String email, Long loanId);
}
