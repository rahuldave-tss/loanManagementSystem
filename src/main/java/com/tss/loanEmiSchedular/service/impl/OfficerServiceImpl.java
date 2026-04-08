package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.LoanDecisionRequestDto;
import com.tss.loanEmiSchedular.dto.response.LoanSummaryResponseDto;
import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.LoanStatus;
import com.tss.loanEmiSchedular.enums.LoanStrategyType;
import com.tss.loanEmiSchedular.events.LoanDecisionEvent;
import com.tss.loanEmiSchedular.exception.BusinessException;
import com.tss.loanEmiSchedular.exception.InvalidPageException;
import com.tss.loanEmiSchedular.exception.ResourceNotFoundException;
import com.tss.loanEmiSchedular.mapper.LoanMapper;
import com.tss.loanEmiSchedular.repository.FinancialProfileRepository;
import com.tss.loanEmiSchedular.repository.LoanRepository;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.FinancialService;
import com.tss.loanEmiSchedular.service.OfficerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfficerServiceImpl implements OfficerService {
    private final LoanMapper loanMapper;
    private final LoanRepository loanRepository;
    private final EmiServiceImpl emiService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final FinancialService financialService;

    @Override
    public List<LoanSummaryResponseDto> viewPendingApplications() {
        List<Loan> loans = loanRepository.findByStatusWithBorrower(LoanStatus.PENDING);

        if (loans.isEmpty()) {
            throw new ResourceNotFoundException("No Pending Application Present");
        }
        return loans.stream().map(loanMapper::toSummaryResponseDto).toList();
    }

    @Override
    public Page<LoanSummaryResponseDto> viewPendingApplicationsByPage(Pageable pageable) {
        Page<Loan> loanPage = loanRepository.findByStatus(LoanStatus.PENDING, pageable);

        if (pageable.getPageNumber() >= loanPage.getTotalPages()) {
            throw new InvalidPageException("Page number out of range");
        }
        return loanPage.map(loanMapper::toSummaryResponseDto);

    }

    @Override
    @Transactional
    public String decideLoan(Long loanId, LoanDecisionRequestDto loanDecisionRequestDto, String officerMail) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan"));

//        System.out.println("loan mil gayi");

        System.out.println(loan);
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new BusinessException("Loan already processed", HttpStatus.CONFLICT);
        }


        User officer = userRepository.findByEmail(officerMail)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

//        System.out.println("user mil gaya");
//        System.out.println(loan.getStatus());

        if (loanDecisionRequestDto.getDecision() == LoanStatus.REJECTED) {
            loan.setStatus(LoanStatus.REJECTED);
            loanRepository.save(loan);
            applicationEventPublisher.publishEvent(new LoanDecisionEvent(loan,
                    loan.getBorrower().getUser().getEmail(),
                    officer));

            log.info("Loan {} rejected by officer",loan.getId());
            return "Loan Rejected";
        }

//        System.out.println("loan not rejected");

        if (loanDecisionRequestDto.getDecision() == LoanStatus.APPROVED) {

            LoanStrategyType finalStrategy =
                    loanDecisionRequestDto.getOverrideStrategy() != null
                            ? loanDecisionRequestDto.getOverrideStrategy()
                            : loan.getSuggestedStrategy();

            loan.setSelectedStrategy(finalStrategy);
            loan.setStatus(LoanStatus.ACTIVE);

            loan.setReminingDebt(loan.getLoanAmount());


            loanRepository.save(loan);

//            System.out.println("loan saved");

            emiService.generateSchedule(loan);
            log.info("Loan {} approved by officer and EMI schedule generated",loan.getId());

            financialService.addFirstEmiToExistingDebt(emiService.calculateBaseEmi(loan), loan.getBorrower().getUser());

            applicationEventPublisher.publishEvent(new LoanDecisionEvent(loan,
                    loan.getBorrower().getUser().getEmail(),
                    officer));


            return "Loan Approved with Strategy: " + finalStrategy;
        }

        throw new BusinessException("Invalid Decision",HttpStatus.BAD_REQUEST);
    }

    @Override
    public LoanSummaryResponseDto viewLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        return loanMapper.toSummaryResponseDto(loan);
    }


}
