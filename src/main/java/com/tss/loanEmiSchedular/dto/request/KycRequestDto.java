package com.tss.loanEmiSchedular.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KycRequestDto {
    private String pan;
    private String name;
    private LocalDate dob;
    private String aadhaar;
    private AddressRequestDto address;
}
