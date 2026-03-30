package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfficerService {
    List<LoanSummaryResponseDto> viewPendingApplications();
    Page<LoanSummaryResponseDto> viewPendingApplicationsByPage(Pageable pageable);
    String decideLoan(Long loanId, LoanDecisionRequestDto loanDecisionRequestDto,String officerMail);
    LoanSummaryResponseDto viewLoan(Long loanId);

}
