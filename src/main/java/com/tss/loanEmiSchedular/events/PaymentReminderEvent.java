package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Emi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReminderEvent {
    private Emi emi;

    public PaymentReminderEvent(Emi emi){
        this.emi=emi;
    }
}
