package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.AuditLogResponseDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.service.AuditLogService;
import com.tss.loanEmiSchedular.service.OfficerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/officer")
@PreAuthorize("hasRole('LOAN_OFFICER')")
@RequiredArgsConstructor
public class OfficerController {

    private final OfficerService officerService;
    private final AuditLogService auditLogService;

    @GetMapping("/applications")
    public ResponseEntity<List<LoanSummaryResponseDto>> getLoanApplications() {
        return new ResponseEntity<>(officerService.viewPendingApplications(), HttpStatus.OK);
    }

    @GetMapping("/applications/pages")
    public ResponseEntity<Page<LoanSummaryResponseDto>> getLoanApplicationsByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(officerService.viewPendingApplicationsByPage(pageable), HttpStatus.OK);
    }

    @PostMapping("/loans/{loanId}/decision")
    public ResponseEntity<String> decideLoan(@PathVariable Long loanId, @Valid @RequestBody LoanDecisionRequestDto loanDecisionRequestDto, Authentication authentication) {
        return new ResponseEntity<>(officerService.decideLoan(loanId, loanDecisionRequestDto,authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<LoanSummaryResponseDto> viewLoanDetails(@PathVariable Long loanId) {
        return new ResponseEntity<>(officerService.viewLoan(loanId), HttpStatus.OK);
    }

    @GetMapping("/loans/{loanId}/audit-logs")
    public ResponseEntity<List<AuditLogResponseDto>> viewAuditLogsByLoan(@PathVariable Long loanId) {
        return new ResponseEntity<>(auditLogService.viewAuditLogsOfLoan(loanId), HttpStatus.OK);
    }

    @GetMapping("/loans/{loanId}/audit-logs/page")
    public ResponseEntity<Page<AuditLogResponseDto>> viewAuditLogsByLoanPage(@PathVariable Long loanId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(auditLogService.viewAuditLogsOfLoanPage(loanId,pageable), HttpStatus.OK);
    }


}
