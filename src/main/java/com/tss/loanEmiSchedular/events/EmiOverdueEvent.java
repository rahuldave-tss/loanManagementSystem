package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.entity.Emi;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmiOverdueEvent {
    private Long emiId;
    private String email;

    public EmiOverdueEvent(Long emiId,String email){
        this.emiId=emiId;
        this.email=email;
    }
}
