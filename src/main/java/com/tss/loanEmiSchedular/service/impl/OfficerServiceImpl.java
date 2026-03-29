package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.events.LoanDecisionEvent;
import com.tss.loanEmiSchedular.mapper.LoanMapper;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.OfficerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficerServiceImpl implements OfficerService {
    private final LoanMapper loanMapper;
    private final LoanRepository loanRepository;
    private final EmiServiceImpl emiService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;

    private final FinancialProfileRepository financialProfileRepository;

    @Override
    public List<LoanSummaryResponseDto> viewPendingApplications() {
        List<Loan> loans=loanRepository.findByStatusWithBorrower(LoanStatus.PENDING);

        if(loans.isEmpty())
        {
            throw new RuntimeException("No Pending Application Present");
        }
        return loans.stream().map(loanMapper::toSummaryResponseDto).toList();
    }

    @Override
    public String decideLoan(Long loanId, LoanDecisionRequestDto loanDecisionRequestDto) {
        Loan loan=loanRepository.findById(loanId)
                .orElseThrow(()->new RuntimeException("Loan not found"));

        System.out.println(loan);
        if(loan.getStatus()!=LoanStatus.PENDING){
            throw new RuntimeException("Loan already processed");
        }

        String email= getLoggedInEmail();
        User officer= userRepository.findByEmail(email)
                        .orElseThrow(()->new RuntimeException("User not found"));

        System.out.println(loan.getStatus());

        if(loanDecisionRequestDto.getDecision()==LoanStatus.REJECTED){
            loan.setStatus(LoanStatus.REJECTED);
            loanRepository.save(loan);
            applicationEventPublisher.publishEvent(new LoanDecisionEvent(loan,
                    loan.getBorrower().getUser().getEmail(),
                    officer));
            return "Loan Rejected";
        }

        System.out.println("loan not rejected");

        if(loanDecisionRequestDto.getDecision()==LoanStatus.APPROVED){

            LoanStrategyType finalStrategy=
                    loanDecisionRequestDto.getOverrideStrategy()!=null
                            ? loanDecisionRequestDto.getOverrideStrategy()
                            : loan.getSuggestedStrategy();

            loan.setSelectedStrategy(finalStrategy);
            loan.setStatus(LoanStatus.ACTIVE);

            loan.setReminingDebt(loan.getLoanAmount());
            String pan = loan.getBorrower().getPan();
            financialProfileRepository.findById(pan).ifPresent(f->f.setExistingDebt(f.getExistingDebt().add(loan.getLoanAmount())));

            loanRepository.save(loan);

            System.out.println("loan saved");

            emiService.generateSchedule(loan);

            applicationEventPublisher.publishEvent(new LoanDecisionEvent(loan,
                    loan.getBorrower().getUser().getEmail(),
                    officer));


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

    private String getLoggedInEmail() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        return authentication.getName();
    }


}
