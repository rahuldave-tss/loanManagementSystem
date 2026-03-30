package com.tss.loanEmiSchedular.service.impl;


import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;
import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.events.LoanAppliedEvent;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.LoanService;
import com.tss.loanEmiSchedular.strategy.LoanStrategy;
import com.tss.loanEmiSchedular.strategy.LoanStrategyFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanStrategyFactory strategyFactory;
    private final FinancialProfileRepository financialProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String applyLoan(LoanApplicationRequest request, String email) {
        log.info("Applying loan for user: {}",email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        System.out.println("user get");

        if (!user.isKycVerified()) {
            log.error("Error while applying loan, KYC not done");
            throw new RuntimeException("Complete KYC first ");
        }
        System.out.println("kyc done");

        Integer activeLoans = loanRepository
                .findByBorrowerAndStatus(user.getBorrowerProfile(), LoanStatus.ACTIVE).size();

        Integer pendingLoans = loanRepository
                .findByBorrowerAndStatus(user.getBorrowerProfile(), LoanStatus.PENDING).size();

        if (activeLoans+pendingLoans >= 3) {
            throw new RuntimeException("Maximum 3 active and pending loans allowed");
        }

        System.out.println("loan is less than 3");
        FinancialProfile profile = financialProfileRepository.findById(user.getBorrowerProfile().getPan())
                .orElseThrow(()-> new RuntimeException("Financial Profile not found"));

        BigDecimal monthlyIncome = profile.getMonthlyIncome();
        BigDecimal existingDebt = profile.getExistingDebt();
        BigDecimal dti = (existingDebt.divide(monthlyIncome,2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
        System.out.println("Dti done");

        LoanStrategy strategy = strategyFactory.getStrategy(dti);

        LoanStrategyType strategyType =
                strategy.decide(dti, request.getTenure());

        System.out.println(strategyType);

        Loan loan = new Loan();

        loan.setBorrower(user.getBorrowerProfile());
        loan.setCreditScore(profile.getCreditScore());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenure(request.getTenure());
        loan.setInterestRate(BigDecimal.valueOf(request.getLoanType().getInterestRate())); // default

        // snapshot values
        loan.setMonthlyIncome(profile.getMonthlyIncome());
        loan.setExistingDebt(existingDebt);
        loan.setDti(dti);
        loan.setReminingDebt(request.getLoanAmount());

        loan.setSuggestedStrategy(strategyType);
        loan.setSelectedStrategy(null);
        loan.setLoanType(request.getLoanType());

        loan.setStatus(LoanStatus.PENDING);
        loan.setDeleted(false);

        loanRepository.save(loan);

        log.info("Loan applied of user: {}",email);

        applicationEventPublisher.publishEvent(new LoanAppliedEvent(loan,email,user));

        return "Loan Applied Successfully Strategy: " + strategyType;
    }
}