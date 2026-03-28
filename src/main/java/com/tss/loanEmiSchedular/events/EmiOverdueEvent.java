package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Emi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmiOverdueEvent {
    private Emi emi;

    public EmiOverdueEvent(Emi emi){
        this.emi=emi;
    }
}
