package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.request.KycRequestDto;

public interface KycService {
    String verifyKyc(KycRequestDto kycRequestDto);
}
