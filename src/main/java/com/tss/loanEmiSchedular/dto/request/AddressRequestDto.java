package com.tss.loanEmiSchedular.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto {
    private String street;
    private String city;
    private String state;
    private String pincode;
}
