package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Loan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class LoanAppliedEvent {
    private final Loan loan;
    private final String email;

    public LoanAppliedEvent(Loan loan,String email){
        this.loan=loan;
        this.email=email;
    }
}
