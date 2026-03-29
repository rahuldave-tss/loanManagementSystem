package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoanDecisionEvent {
    private final Loan loan;
    private final String email;
    private final User officer;

    public LoanDecisionEvent(Loan loan,String email,User officer){
        this.loan=loan;
        this.email=email;
        this.officer=officer;
    }
}
