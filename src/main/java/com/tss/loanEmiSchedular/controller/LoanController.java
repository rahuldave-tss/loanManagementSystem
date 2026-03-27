package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;
import com.tss.loanEmiSchedular.service.LoanService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PreAuthorize("hasRole('BORROWER')")
    @PostMapping("/apply")
    public String applyLoan(@RequestBody LoanApplicationRequest request, Authentication authentication) {

        String email = authentication.getName();

        return loanService.applyLoan(request, email);
    }

}