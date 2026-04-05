package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.events.LoanDecisionEvent;
import com.tss.loanEmiSchedular.mapper.LoanMapper;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.FinancialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfficerServiceImplTest {
    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EmiServiceImpl emiService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FinancialService financialService;

    @InjectMocks
    private OfficerServiceImpl officerService;

    @Test
    void shouldThrowWhenNoPendingApplications() {
        when(loanRepository.findByStatusWithBorrower(LoanStatus.PENDING))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> officerService.viewPendingApplications());

        assertEquals("No Pending Application Present not found", ex.getMessage());
    }

    @Test
    void shouldReturnPendingApplications() {
        Loan loan = new Loan();
        LoanSummaryResponseDto dto = new LoanSummaryResponseDto();

        when(loanRepository.findByStatusWithBorrower(LoanStatus.PENDING))
                .thenReturn(List.of(loan));

        when(loanMapper.toSummaryResponseDto(loan))
                .thenReturn(dto);

        List<LoanSummaryResponseDto> result = officerService.viewPendingApplications();

        assertEquals(1, result.size());
    }
    @Test
    void shouldRejectLoan() {


        Loan loan = new Loan();

        loan.setStatus(LoanStatus.PENDING);

        User officer = new User();
        officer.setEmail("temp@gmail.com");

        BorrowerProfile borrowerProfile = new BorrowerProfile();

        borrowerProfile.setUser(officer);
        loan.setBorrower(borrowerProfile);

        LoanDecisionRequestDto request = new LoanDecisionRequestDto();
        request.setDecision(LoanStatus.REJECTED);

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(officer));

        when(loanRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        String result = officerService.decideLoan(1L, request, "officer@mail.com");

        assertEquals("Loan Rejected", result);

        verify(applicationEventPublisher).publishEvent(any(LoanDecisionEvent.class));
    }

    @Test
    void shouldApproveLoan() {

        BorrowerProfile borrowerProfile = new BorrowerProfile();
        Loan loan = new Loan();
        loan.setBorrower(borrowerProfile);
        loan.setStatus(LoanStatus.PENDING);
        loan.setSuggestedStrategy(LoanStrategyType.STEP_UP);
        loan.setLoanAmount(new BigDecimal("10000"));

        User officer = new User();
        borrowerProfile.setUser(officer);
        LoanDecisionRequestDto request = new LoanDecisionRequestDto();
        request.setDecision(LoanStatus.APPROVED);

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(officer));

        when(loanRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        when(emiService.calculateBaseEmi(any()))
                .thenReturn(new BigDecimal("1000"));

        String result = officerService.decideLoan(1L, request, "officer@mail.com");

        assertTrue(result.contains("Loan Approved"));

        verify(emiService).generateSchedule(any());
        verify(financialService).addFirstEmiToExistingDebt(any(), any());
        verify(applicationEventPublisher).publishEvent(any(LoanDecisionEvent.class));
    }
    @Test
    void shouldThrowWhenLoanNotFound() {
        when(loanRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> officerService.decideLoan(1L, new LoanDecisionRequestDto(), "test@mail.com"));

        assertEquals("Loan not found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenLoanAlreadyProcessed() {
        Loan loan = new Loan();
        loan.setStatus(LoanStatus.ACTIVE);

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> officerService.decideLoan(1L, new LoanDecisionRequestDto(), "test@mail.com"));

        assertEquals("Loan already processed", ex.getMessage());
    }
}