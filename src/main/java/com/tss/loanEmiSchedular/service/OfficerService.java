package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;

import java.util.List;

public interface OfficerService {
    List<LoanSummaryResponseDto> viewPendingApplications();
    String decideLoan(Long loanId, LoanDecisionRequestDto loanDecisionRequestDto);
    LoanSummaryResponseDto viewLoan(Long loanId);

}
