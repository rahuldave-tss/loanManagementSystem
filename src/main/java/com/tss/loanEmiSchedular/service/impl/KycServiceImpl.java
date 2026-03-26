package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.KycRequestDto;
import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.repository.BorrowerRepository;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.service.KycService;
import com.tss.loanEmiSchedular.util.PanHashUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class KycServiceImpl implements KycService {
    private final FinancialProfileRepository financialProfileRepository;
    private final BorrowerRepository borrowerRepository;

    @Override
    public String verifyKyc(KycRequestDto kycRequestDto) {
        String normalizedPan=kycRequestDto.getPan().toUpperCase().trim();

        String hashedPan= PanHashUtil.hashPan(normalizedPan);

        System.out.println("Hashed pan: "+hashedPan);

        FinancialProfile fp= financialProfileRepository.findById(hashedPan)
                .orElseThrow(()->new RuntimeException("Invalid PAN"));

        BorrowerProfile borrowerProfile=new BorrowerProfile();
        borrowerProfile.setPan(hashedPan);

        borrowerProfile.getUser().setIsKycVerified(true);

        borrowerRepository.save(borrowerProfile);

        return "KYC successful";

    }
}
