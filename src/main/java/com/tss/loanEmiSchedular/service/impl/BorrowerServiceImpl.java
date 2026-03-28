package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.response.BorrowerLoanResponseDto;
import com.tss.loanEmiSchedular.dto.response.EmiResponseDto;
import com.tss.loanEmiSchedular.mapper.BorrowerMapper;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.PaymentRepository;
import com.tss.loanEmiSchedular.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowerServiceImpl implements BorrowerService {
    private final LoanRepository loanRepository;
    private final EmiRepository emiRepository;
    private final PaymentRepository paymentRepository;
    private final BorrowerMapper borrowerMapper;   // ← injected by Spring

    // ── 1. All loans ─────────────────────────────────────────────────────────
    @Override
    public List<BorrowerLoanResponseDto> getMyLoans(String email) {
        return borrowerMapper.toLoanDtoList(
                loanRepository.findByBorrowerUserEmailAndIsDeletedFalse(email)
        );
    }

    // ── 2. All EMIs for a loan ────────────────────────────────────────────────
    @Override
    public List<EmiResponseDto> getEmisForLoan(String email, Long loanId) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!loan.getBorrower().getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied: this loan does not belong to you");
        }

        return borrowerMapper.toEmiResponseDtoList(
                emiRepository.findByLoanIdOrderByInstallmentNumberAsc(loanId)
        );
    }
}
