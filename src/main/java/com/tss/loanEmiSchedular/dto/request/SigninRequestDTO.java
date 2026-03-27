package com.tss.loanEmiSchedular.dto.request;

import com.tss.loanEmiSchedular.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequestDTO {
    private String email;
    private String password;

    private Role role;
}