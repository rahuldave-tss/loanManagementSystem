package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.AuditLogResponseDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.service.AuditLogService;
import com.tss.loanEmiSchedular.service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officer")
@PreAuthorize("hasRole('LOAN_OFFICER')")
@RequiredArgsConstructor
public class OfficerController {

    private final OfficerService officerService;
    private final AuditLogService auditLogService;

    @GetMapping("/applications")
    public ResponseEntity<List<LoanSummaryResponseDto>> getLoanApplications(){
        return new ResponseEntity<>(officerService.viewPendingApplications(), HttpStatus.OK);
    }

    @PostMapping("/loans/{loanId}/decision")
    public ResponseEntity<String> decideLoan(@PathVariable Long loanId, @RequestBody LoanDecisionRequestDto loanDecisionRequestDto){
        return new ResponseEntity<>(officerService.decideLoan(loanId,loanDecisionRequestDto),HttpStatus.OK);
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<LoanSummaryResponseDto> viewLoanDetails(@PathVariable Long loanId){
        return new ResponseEntity<>(officerService.viewLoan(loanId),HttpStatus.OK);
    }

    @GetMapping("/loans/{loanId}/audit-logs")
    public ResponseEntity<List<AuditLogResponseDto>> viewAuditLogsByLoan(@PathVariable Long loanId){
        return new ResponseEntity<>(auditLogService.viewAuditLogsOfLoan(loanId),HttpStatus.OK);
    }


}
