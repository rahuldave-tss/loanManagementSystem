package com.tss.loanEmiSchedular.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String message;
    private String token;

    public AuthResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }
}