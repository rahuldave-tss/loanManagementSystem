package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Loan;
import com.tss.loanEmiSchedular.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class LoanAppliedEvent {
    private final Loan loan;
    private final String email;
    private final User user;

    public LoanAppliedEvent(Loan loan,String email,User user){
        this.loan=loan;
        this.email=email;
        this.user=user;
    }
}
