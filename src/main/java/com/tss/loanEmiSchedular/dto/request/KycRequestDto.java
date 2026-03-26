package com.tss.loanEmiSchedular.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KycRequestDto {
    private String pan;
    private String aadhaar;
    private AddressRequestDto address;
}
