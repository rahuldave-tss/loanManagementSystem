package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.exception.ResourceNotFoundException;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.service.FinancialService;
import com.tss.loanEmiSchedular.util.PanHashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.ReadOnlyFileSystemException;

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl implements FinancialService {
    private static final Logger log = LoggerFactory.getLogger(FinancialServiceImpl.class);
    private final FinancialProfileRepository financialProfileRepository;

    @Override
    public void addFirstEmiToExistingDebt(BigDecimal firstEmi, User user) {
        String normalPan=user.getBorrowerProfile().getPan();

        FinancialProfile fp = financialProfileRepository
                .findById(normalPan)
                .orElseThrow(() -> new ResourceNotFoundException("Financial Profile not found"));

        fp.setExistingDebt(fp.getExistingDebt().add(firstEmi));

        financialProfileRepository.save(fp);

//        System.out.println("Existing debt: "+fp.getExistingDebt());
//        System.out.println("First EMI: "+firstEmi);

    }
}
