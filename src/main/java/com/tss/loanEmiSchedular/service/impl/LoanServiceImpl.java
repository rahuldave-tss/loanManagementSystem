package com.tss.loanEmiSchedular.service.impl;


import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;
import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.LoanService;
import com.tss.loanEmiSchedular.strategy.LoanStrategy;
import com.tss.loanEmiSchedular.strategy.LoanStrategyFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanStrategyFactory strategyFactory;
    private final FinancialProfileRepository financialProfileRepository;

    @Override
    public String applyLoan(LoanApplicationRequest request, String email) {

        // 🔹 1. Get Borrower
        User user = userRepository.findByEmail(email).get();
        System.out.println("user get");
        // 🔹 2. KYC Check
        if (!user.isKycVerified()) {
            throw new RuntimeException("Complete KYC first ");
        }
        System.out.println("kyc done");
        // 🔹 3. Get ACTIVE Loans
        List<Loan> activeLoans = loanRepository
                .findByBorrowerAndStatus(user.getBorrowerProfile(), LoanStatus.ACTIVE);

        List<Loan> pendingLoans = loanRepository
                .findByBorrowerAndStatus(user.getBorrowerProfile(), LoanStatus.PENDING);

        if (activeLoans.size()+pendingLoans.size() >= 3) {
            throw new RuntimeException("Maximum 3 active and pending loans allowed");
        }

        System.out.println("loan is less than 3");
        FinancialProfile profile = financialProfileRepository.findById(user.getBorrowerProfile().getPan()).get();

        // 🔹 5. Calculate DTI
        BigDecimal monthlyIncome = profile.getMonthlyIncome();
        BigDecimal existingDebt = profile.getExistingDebt();
        BigDecimal dti = (existingDebt.divide(monthlyIncome,2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
        System.out.println("Dti done");
        // 🔹 6. Get Strategy using Factory
        LoanStrategy strategy = strategyFactory.getStrategy(dti);

        LoanStrategyType strategyType =
                strategy.decide(dti, request.getTenure());

        System.out.println(strategyType);
        // 🔹 7. Create Loan Object
        Loan loan = new Loan();

        loan.setBorrower(user.getBorrowerProfile());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenure(request.getTenure());
        loan.setInterestRate(BigDecimal.valueOf(10.0)); // default

        // snapshot values
        loan.setMonthlyIncome(profile.getMonthlyIncome());
        loan.setExistingDebt(existingDebt);
        loan.setDti(dti);

        loan.setSuggestedStrategy(strategyType);
        loan.setSelectedStrategy(null);
        loan.setLoanType(request.getLoanType());

        loan.setStatus(LoanStatus.PENDING);
        loan.setDeleted(false);

        // 🔹 8. Save Loan
        loanRepository.save(loan);

        return "Loan Applied Successfully Strategy: " + strategyType;
    }


    // 🔥 EMI Calculation Utility
    private double calculateEmi(double amount, double annualRate, int tenure) {

        double monthlyRate = annualRate / 12 / 100;

        return (amount * monthlyRate * Math.pow(1 + monthlyRate, tenure)) /
                (Math.pow(1 + monthlyRate, tenure) - 1);
    }
}