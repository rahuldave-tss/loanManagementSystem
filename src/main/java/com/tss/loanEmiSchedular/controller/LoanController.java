package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;
import com.tss.loanEmiSchedular.dto.response.LoanTypeResponse;
import com.tss.loanEmiSchedular.service.LoanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/v1/borrower/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PreAuthorize("hasRole('BORROWER')")
    @PostMapping("/apply")
    public ResponseEntity<String> applyLoan(@Valid @RequestBody LoanApplicationRequest request, Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.applyLoan(request, email));
    }

    @GetMapping("/types")
    public ResponseEntity<List<LoanTypeResponse>> getLoanTypes() {
        return ResponseEntity.ok(loanService.getAllLoanTypes());
    }
}