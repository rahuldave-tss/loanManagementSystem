package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Loan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoanDecisionEvent {
    private final Loan loan;
    private final String email;

    public LoanDecisionEvent(Loan loan,String email){
        this.loan=loan;
        this.email=email;
    }
}
