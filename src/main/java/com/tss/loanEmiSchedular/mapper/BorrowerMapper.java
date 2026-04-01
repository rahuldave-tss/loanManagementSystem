package com.tss.loanEmiSchedular.mapper;

import com.tss.loanEmiSchedular.dto.response.BorrowerLoanResponseDto;
import com.tss.loanEmiSchedular.dto.response.EmiResponseDto;
import com.tss.loanEmiSchedular.dto.response.PaymentHistoryResponseDto;
import com.tss.loanEmiSchedular.dto.response.PaymentResponseDto;
import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowerMapper {

    // ── Loan → BorrowerLoanResponseDto ──────────────────────────────────────
    @Mapping(source = "id",               target = "loanId")
    @Mapping(source = "loanType",         target = "loanType")
    @Mapping(source = "status",           target = "status")
    BorrowerLoanResponseDto toLoanResponseDto(Loan loan);

    List<BorrowerLoanResponseDto> toLoanDtoList(List<Loan> loans);

    // ── Emi → EmiResponseDto ─────────────────────────────────────────────────
    @Mapping(source = "id",        target = "emiId")
    @Mapping(source = "emiStatus", target = "emiStatus")  // enum → String
    EmiResponseDto toEmiDto(Emi emi);

    List<EmiResponseDto> toEmiResponseDtoList(List<Emi> emis);

    // ── Payment → PaymentResponseDto ─────────────────────────────────────────
    @Mapping(source = "emi.id",                target = "emiId")
    @Mapping(source = "emi.installmentNumber", target = "installmentNumber")
    @Mapping(source = "amount",                target = "amountPaid")
    @Mapping(source = "paymentStatus",         target = "paymentStatus")
    @Mapping(target = "message",               ignore = true)
    @Mapping(source = "emi.loan.id" ,target = "loanId")
    PaymentResponseDto toPaymentDto(Payment payment);

    // ── Payment → PaymentHistoryResponseDto ──────────────────────────────────
    @Mapping(source = "id",                    target = "paymentId")
    @Mapping(source = "emi.id",                target = "emiId")
    @Mapping(source = "emi.installmentNumber", target = "installmentNumber")
    @Mapping(source = "paymentStatus",         target = "paymentStatus")
    @Mapping(source = "createdAt",             target = "paidAt")
    @Mapping(source = "emi.loan.id" ,target = "loanId")
    PaymentHistoryResponseDto toPaymentHistoryDto(Payment payment);

    List<PaymentHistoryResponseDto> toPaymentHistoryDtoList(List<Payment> payments);
}