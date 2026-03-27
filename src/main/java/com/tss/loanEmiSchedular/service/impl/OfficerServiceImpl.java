package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.mapper.LoanMapper;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficerServiceImpl implements OfficerService {
    private final LoanMapper loanMapper;
    private final LoanRepository loanRepository;
    private final EmiServiceImpl emiService;


    @Override
    public List<LoanSummaryResponseDto> viewPendingApplications() {
        List<Loan> loans=loanRepository.findByStatusWithBorrower(LoanStatus.PENDING);
        return loans.stream().map(loanMapper::toSummaryResponseDto).toList();
    }

    @Override
    public String decideLoan(Long loanId, LoanDecisionRequestDto loanDecisionRequestDto) {
        Loan loan=loanRepository.findById(loanId)
                .orElseThrow(()->new RuntimeException("Loan not found"));

        if(loan.getStatus()!=LoanStatus.PENDING){
            throw new RuntimeException("Loan already processed");
        }

        if(loanDecisionRequestDto.getDecision()==LoanStatus.REJECTED){
            loan.setStatus(LoanStatus.REJECTED);
            loanRepository.save(loan);
            return "Loan Rejected";
        }

        if(loanDecisionRequestDto.getDecision()==LoanStatus.ACTIVE){

            LoanStrategyType finalStrategy=
                    loanDecisionRequestDto.getOverrideStrategy()!=null
                            ? loanDecisionRequestDto.getOverrideStrategy()
                            : loan.getSuggestedStrategy();

            loan.setSelectedStrategy(finalStrategy);
            loan.setStatus(LoanStatus.ACTIVE);

            loanRepository.save(loan);

            emiService.generateSchedule(loan);

            return "Loan Approved with Strategy: "+finalStrategy;
        }

        throw new RuntimeException("Invalid Decision");
    }

    @Override
    public LoanSummaryResponseDto viewLoan(Long loanId) {
        Loan loan=loanRepository.findById(loanId)
                .orElseThrow(()->new RuntimeException("Loan not found"));

        return loanMapper.toSummaryResponseDto(loan);
    }


}
