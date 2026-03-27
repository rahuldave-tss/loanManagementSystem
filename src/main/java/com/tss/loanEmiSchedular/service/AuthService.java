package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.dto.request.SigninRequestDTO;
import com.tss.loanEmiSchedular.dto.request.SignupRequestDTO;
import com.tss.loanEmiSchedular.dto.response.AuthResponseDTO;

public interface AuthService {
    //signup
    String signUp(SignupRequestDTO dto);

    //signin
    AuthResponseDTO signIn(SigninRequestDTO dto);
}
