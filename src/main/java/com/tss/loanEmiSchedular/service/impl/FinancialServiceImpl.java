package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.service.FinancialService;
import com.tss.loanEmiSchedular.util.PanHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl implements FinancialService {
    private final FinancialProfileRepository financialProfileRepository;

    @Override
    public void addFirstEmiToExistingDebt(BigDecimal firstEmi, User user) {
        String normalPan=user.getBorrowerProfile().getPan();

        String hashedPan= PanHashUtil.hashPan(normalPan);

        FinancialProfile fp = financialProfileRepository
                .findById(hashedPan)
                .orElseThrow(() -> new RuntimeException("Financial Profile not found"));

        fp.setExistingDebt(fp.getExistingDebt().add(firstEmi));

    }
}
