package com.tss.loanEmiSchedular.mapper;

import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(source = "borrower.user.email",target = "borrowerEmail")
    @Mapping(source = "id",target = "loanId")
    LoanSummaryResponseDto toSummaryResponseDto(Loan loan);
}
