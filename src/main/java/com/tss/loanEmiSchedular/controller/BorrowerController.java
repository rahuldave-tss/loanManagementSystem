package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.KycRequestDto;
import com.tss.loanEmiSchedular.service.impl.KycServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@PreAuthorize("hasRole('BORROWER')")
@RequestMapping("/borrower")
public class BorrowerController {
    private final KycServiceImpl kycService;

    @PostMapping("/kyc")
    public ResponseEntity<String> getBorrowerInfoForKyc(@RequestBody KycRequestDto kycRequestDto){
        return new ResponseEntity<>(kycService.verifyKyc(kycRequestDto), HttpStatus.OK);
    }


}
