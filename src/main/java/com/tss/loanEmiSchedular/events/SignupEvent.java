package com.tss.loanEmiSchedular.events;

import com.tss.loanEmiSchedular.dto.request.SignupRequestDTO;

public class SignupEvent {

    private final String email;
    private final String otp;

    public SignupEvent(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }
}