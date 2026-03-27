package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;

public interface LoanService {
    String applyLoan(LoanApplicationRequest request, String email);
}
