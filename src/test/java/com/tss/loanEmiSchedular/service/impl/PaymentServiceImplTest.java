package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.response.PaymentResponseDto;
import com.tss.loanEmiSchedular.entity.BorrowerProfile;
import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.events.EmiPaidEvent;
import com.tss.loanEmiSchedular.mapper.BorrowerMapper;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.PaymentRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private LoanRepository loanRepository;
    @Mock private EmiRepository emiRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private BorrowerMapper borrowerMapper;
    @Mock private FinancialProfileRepository financialProfileRepository;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void shouldPayEmiSuccessfully() {

        Loan loan=mockLoan("test@mail.com");

        Emi emi = new Emi();
        emi.setLoan(loan);
        emi.setInstallmentNumber(1);
        emi.setTotalDueAmount(BigDecimal.valueOf(500));
        emi.setPrincipal(BigDecimal.valueOf(400));

        List<Emi> unpaid = List.of(emi);

        when(loanRepository.findById(loan.getId())).thenReturn(Optional.of(loan));
        when(emiRepository.findUnpaidEmisByLoanIdOrdered(loan.getId())).thenReturn(unpaid);
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(borrowerMapper.toPaymentDto(any())).thenReturn(new PaymentResponseDto());

        PaymentResponseDto result = paymentService.payEmi("test@mail.com", loan.getId());

        assertNotNull(result);

        verify(emiRepository).save(any());
        verify(paymentRepository).save(any());
        verify(eventPublisher).publishEvent(any(EmiPaidEvent.class));
    }

    @Test
    void shouldThrowWhenLoanNotFound(){
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,()->{
            paymentService.payEmi("test@mail.com",1L);
        });
    }

    @Test
    void shouldThrowWhenUserNotOwner() {

        Loan loan = new Loan();
        loan.setStatus(LoanStatus.ACTIVE);

        User user = new User();
        user.setEmail("other@mail.com");

        BorrowerProfile borrower = new BorrowerProfile();
        borrower.setUser(user);

        loan.setBorrower(borrower);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(RuntimeException.class, () -> {
            paymentService.payEmi("test@mail.com", 1L);
        });
    }

    @Test
    void shouldThrowWhenEmiNotGenerated() {

        Loan loan = mockLoan("test@mail.com");

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(emiRepository.findUnpaidEmisByLoanIdOrdered(1L)).thenReturn(List.of());
        when(emiRepository.existsByLoanId(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            paymentService.payEmi("test@mail.com", 1L);
        });
    }

    private Loan mockLoan(String email) {
        Loan loan = new Loan();
        loan.setStatus(LoanStatus.ACTIVE);

        User user = new User();
        user.setEmail(email);

        BorrowerProfile borrower = new BorrowerProfile();
        borrower.setUser(user);
        borrower.setPan("PAN123");

        loan.setBorrower(borrower);
        loan.setReminingDebt(BigDecimal.valueOf(1000));

        return loan;
    }

}