package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.LoanApplicationRequest;
import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import com.tss.loanEmiSchedular.entity.FinancialProfile;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.enums.LoanType;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.EmiService;
import com.tss.loanEmiSchedular.service.LoanService;
import com.tss.loanEmiSchedular.strategy.LoanStrategy;
import com.tss.loanEmiSchedular.strategy.LoanStrategyFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private FinancialProfileRepository financialProfileRepository;

    @Mock
    private LoanStrategyFactory loanStrategyFactory;

    @Mock
    private EmiService emiService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loanService.applyLoan(new LoanApplicationRequest(), "test@mail.com")
        );

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenKycNotDone() {
        User user = new User();
        user.setKycVerified(false);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loanService.applyLoan(new LoanApplicationRequest(), "test@mail.com")
        );

        assertEquals("Complete KYC first ", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoanLimitExceeded() {
        User user = new User();
        user.setKycVerified(true);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(loanRepository.findByBorrowerAndStatus(any(), eq(LoanStatus.ACTIVE)))
                .thenReturn(List.of(new Loan(), new Loan()));

        when(loanRepository.findByBorrowerAndStatus(any(), eq(LoanStatus.PENDING)))
                .thenReturn(List.of(new Loan()));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loanService.applyLoan(new LoanApplicationRequest(), "test@mail.com")
        );

        assertEquals("Maximum 3 active and pending loans allowed", ex.getMessage());
    }

    @Test
    void shouldRejectLoanWhenFinalDtiGreaterThan40() {

        // Arrange
        User user = new User();
        user.setKycVerified(true);

        BorrowerProfile borrowerProfile = new BorrowerProfile();
        borrowerProfile.setPan("PAN123");

        user.setBorrowerProfile(borrowerProfile);

        FinancialProfile profile = new FinancialProfile();
        profile.setMonthlyIncome(new BigDecimal("10000"));
        profile.setExistingDebt(new BigDecimal("3000"));
        profile.setCreditScore(750);

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setTenure(12);
        request.setLoanAmount(new BigDecimal("10000"));
        request.setLoanType(LoanType.PERSONAL);

        LoanStrategy strategy = mock(LoanStrategy.class);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(loanRepository.findByBorrowerAndStatus(any(), any()))
                .thenReturn(List.of());

        when(financialProfileRepository.findById(any()))
                .thenReturn(Optional.of(profile));

        when(loanStrategyFactory.getStrategy(any()))
                .thenReturn(strategy);

        when(strategy.decide(any(), anyInt()))
                .thenReturn(LoanStrategyType.STEP_UP);

        when(emiService.calculateBaseEmi(any()))
                .thenReturn(new BigDecimal("2000")); // high EMI

        when(loanRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = loanService.applyLoan(request, "test@mail.com");

        // Assert
        assertEquals("Loan Rejected Due to High DTI", result);
    }
    @Test
    void shouldApplyLoanSuccessfully() {

        User user = new User();
        user.setKycVerified(true);
        BorrowerProfile borrowerProfile = new BorrowerProfile();
        borrowerProfile.setPan("PAN123");
        user.setBorrowerProfile(borrowerProfile);

        FinancialProfile profile = new FinancialProfile();
        profile.setMonthlyIncome(new BigDecimal("20000"));
        profile.setExistingDebt(new BigDecimal("2000"));
        profile.setCreditScore(750);


        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setTenure(12);
        request.setLoanAmount(new BigDecimal("10000"));
        request.setLoanType(LoanType.PERSONAL);

        LoanStrategy strategy = mock(LoanStrategy.class);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(loanRepository.findByBorrowerAndStatus(any(), any()))
                .thenReturn(List.of());

        when(financialProfileRepository.findById(any()))
                .thenReturn(Optional.of(profile));

        when(loanStrategyFactory.getStrategy(any()))
                .thenReturn(strategy);

        when(strategy.decide(any(), anyInt()))
                .thenReturn(LoanStrategyType.STEP_UP);

        when(emiService.calculateBaseEmi(any()))
                .thenReturn(new BigDecimal("1000"));

        when(loanRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = loanService.applyLoan(request, "test@mail.com");

        // Assert
        assertTrue(result.contains("Loan Applied Successfully"));
    }
}