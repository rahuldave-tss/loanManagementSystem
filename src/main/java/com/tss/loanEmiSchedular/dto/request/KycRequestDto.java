package com.tss.loanEmiSchedular.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KycRequestDto {
    @NotBlank(message = "PAN is required")
    private String pan;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "DOB is required")
    @Past(message = "DOB must be in the past")
    private LocalDate dob;
    private String aadhaar;

    @NotNull(message = "Address is required")
    @Valid
    private AddressRequestDto address;
}
