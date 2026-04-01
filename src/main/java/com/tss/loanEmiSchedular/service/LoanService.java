package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;
import com.tss.loanEmiSchedular.dto.response.LoanTypeResponse;

import java.util.List;

public interface LoanService {
    String applyLoan(LoanApplicationRequest request, String email);

    List<LoanTypeResponse> getAllLoanTypes();
}
