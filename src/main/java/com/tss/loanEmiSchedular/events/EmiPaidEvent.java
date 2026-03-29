package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Emi;
import com.tss.loanEmiSchedular.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmiPaidEvent {
    private final Emi emi;
    private final User borrower;

    public EmiPaidEvent(Emi emi, User borrower) {
        this.emi = emi;
        this.borrower = borrower;
    }
}
