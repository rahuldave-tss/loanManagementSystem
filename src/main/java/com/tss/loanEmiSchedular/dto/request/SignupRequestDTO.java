package com.tss.loanEmiSchedular.dto.request;


import com.tss.loanEmiSchedular.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    private String email;
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}