package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.response.PaymentHistoryResponseDto;
import com.tss.loanEmiSchedular.dto.response.PaymentResponseDto;
import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.Payment;
import com.tss.loanEmiSchedular.enums.EmiStatus;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.PaymentStatus;
import com.tss.loanEmiSchedular.events.EmiPaidEvent;
import com.tss.loanEmiSchedular.mapper.BorrowerMapper;
import com.tss.loanEmiSchedular.repository.EmiRepository;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.PaymentRepository;
import com.tss.loanEmiSchedular.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final LoanRepository loanRepository; // add this
    private final EmiRepository emiRepository;
    private final PaymentRepository paymentRepository;
    private final BorrowerMapper borrowerMapper;
    private final FinancialProfileRepository financialProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public PaymentResponseDto payEmi(String email, Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if(loan.getStatus()!=LoanStatus.ACTIVE){
            throw new RuntimeException("You can only pay EMIs of ACTIVE loan");
        }

        if (!loan.getBorrower().getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied: this loan does not belong to you");
        }

        List<Emi> unpaid = emiRepository.findUnpaidEmisByLoanIdOrdered(loanId);

        if (unpaid.isEmpty()) {
            boolean emisExist=emiRepository.existsByLoanId(loanId);
            if(!emisExist){
                throw new RuntimeException("EMIs are not generated yet!");
            }
            else{
                throw new RuntimeException("All EMIs are already paid for this loan");
            }
        }

        Emi emi = unpaid.get(0);

        Emi nextEmi;
        if(unpaid.size()==1){
            nextEmi=null;
        }
        else nextEmi=unpaid.get(1);

        String txnId = "TXN-" + UUID.randomUUID().toString().toUpperCase().substring(0, 12);

        if (!simulateGateway()) {
            Payment failed = new Payment();
            failed.setEmi(emi);
            failed.setAmount(emi.getTotalDueAmount());
            failed.setPaymentStatus(PaymentStatus.FAILED);
            failed.setTransactionId(txnId);
            paymentRepository.save(failed);

            PaymentResponseDto dto = borrowerMapper.toPaymentDto(failed);
            dto.setMessage("Payment gateway failed. Please try again.");
            return dto;
        }

        Payment payment = processPayment(emi,nextEmi, txnId);
        log.info("EMI #{} paid for loan {}. TxnId: {}", emi.getInstallmentNumber(), loanId, txnId);

        PaymentResponseDto dto = borrowerMapper.toPaymentDto(payment);
        dto.setMessage("EMI #" + emi.getInstallmentNumber() + " paid successfully.");

        applicationEventPublisher.publishEvent(new EmiPaidEvent(emi,emi.getLoan().getBorrower().getUser()));
        return dto;
    }

    @Override
    public List<PaymentHistoryResponseDto> getPaymentHistoryByEmi(String email, Long emiId) {

        Emi emi = emiRepository.findById(emiId)
                .orElseThrow(() -> new RuntimeException("EMI not found"));

        if (!emi.getLoan().getBorrower().getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied: this EMI does not belong to you");
        }
        List<Payment> payments = paymentRepository.findByEmiId(emiId);

        if(payments.isEmpty())
            throw new RuntimeException("No Payment History available");

        return borrowerMapper.toPaymentHistoryDtoList(
                payments
        );
    }

    @Override
    public List<PaymentHistoryResponseDto> getPaymentHistory(String email, Long loanId)
    {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
        if (!loan.getBorrower().getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied: this EMI does not belong to you");
        }
        List<Payment> payments = paymentRepository.findByEmiLoanId(loanId);

        if(payments.isEmpty())
            throw new RuntimeException("No Payment History available");

        return borrowerMapper.toPaymentHistoryDtoList(
                payments
        );
    }

    private Payment processPayment(Emi emi,Emi nextEmi, String txnId) {
        BigDecimal amount = emi.getTotalDueAmount() != null
                ? emi.getTotalDueAmount()
                : emi.getRemainingAmount();

        emi.setFullyPaid(true);
        emi.setEmiStatus(EmiStatus.PAID);
        emi.setTotalPaidAmount(amount);
        emi.setRemainingAmount(BigDecimal.ZERO);
        emiRepository.save(emi);

        //update remaining debt
        emi.getLoan().setReminingDebt(emi.getLoan().getReminingDebt().subtract(emi.getPrincipal()));

        //if remaining debt is zero then update loan status
        if (emi.getLoan().getReminingDebt().compareTo(BigDecimal.ZERO) == 0)
            emi.getLoan().setStatus(LoanStatus.CLOSED);

        //update existing debt
        financialProfileRepository.findById(emi.getLoan().getBorrower().getPan()).ifPresent(f->f.setExistingDebt(f.getExistingDebt().subtract(emi.getTotalDueAmount())));

        if(nextEmi!=null){
            financialProfileRepository.findById(emi.getLoan().getBorrower().getPan()).ifPresent(f->f.setExistingDebt(f.getExistingDebt().add(nextEmi.getTotalDueAmount())));
        }

        Payment payment = new Payment();
        payment.setEmi(emi);
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(txnId);
        return paymentRepository.save(payment);
    }

    private boolean simulateGateway() {
        return true;
    }
}