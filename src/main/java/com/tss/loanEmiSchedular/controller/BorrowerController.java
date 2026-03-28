package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.KycRequestDto;
import com.tss.loanEmiSchedular.dto.response.BorrowerLoanResponseDto;
import com.tss.loanEmiSchedular.dto.response.EmiResponseDto;
import com.tss.loanEmiSchedular.dto.response.PaymentHistoryResponseDto;
import com.tss.loanEmiSchedular.dto.response.PaymentResponseDto;
import com.tss.loanEmiSchedular.service.BorrowerService;
import com.tss.loanEmiSchedular.service.PaymentService;
import com.tss.loanEmiSchedular.service.impl.KycServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@PreAuthorize("hasRole('BORROWER')")
@RequestMapping("/borrower")
public class BorrowerController {
    private final KycServiceImpl kycService;
    private final BorrowerService borrowerService; // add this alongside KycServiceImpl
    private final PaymentService paymentService;

    @PostMapping("/kyc")
    public ResponseEntity<String> getBorrowerInfoForKyc(@RequestBody KycRequestDto kycRequestDto) {
        return new ResponseEntity<>(kycService.verifyKyc(kycRequestDto), HttpStatus.OK);
    }


    // GET /borrower/loans
    @GetMapping("/loans")
    public ResponseEntity<List<BorrowerLoanResponseDto>> getMyLoans(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(borrowerService.getMyLoans(authentication.getName()));
    }

    // GET /borrower/loans/{loanId}/emis
    @GetMapping("/loans/{loanId}/emis")
    public ResponseEntity<List<EmiResponseDto>> getEmis(@PathVariable Long loanId, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(borrowerService.getEmisForLoan(authentication.getName(), loanId));
    }

    @PostMapping("/loans/{loanId}/emis/pay")
    public ResponseEntity<PaymentResponseDto> payEmi(@PathVariable Long loanId, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.payEmi(authentication.getName(), loanId));
    }

    @GetMapping("/loans/emi/{emiId}/history")
    public ResponseEntity<List<PaymentHistoryResponseDto>> getHistoryByEmi(@PathVariable Long emiId, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentHistoryByEmi(authentication.getName(), emiId));
    }

    @GetMapping("/loans/{loanId}/history")
    public ResponseEntity<List<PaymentHistoryResponseDto>> getHistory(@PathVariable Long loanId,Authentication authentication)
    {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentHistory(authentication.getName(), loanId));
    }
}
