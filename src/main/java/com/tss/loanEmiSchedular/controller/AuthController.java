package com.tss.loanEmiSchedular.controller;

import com.tss.loanEmiSchedular.dto.request.SigninRequestDTO;
import com.tss.loanEmiSchedular.dto.request.SignupRequestDTO;
import com.tss.loanEmiSchedular.dto.request.VerificationDto;
import com.tss.loanEmiSchedular.dto.response.AuthResponseDTO;
import com.tss.loanEmiSchedular.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.signUp(dto));
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerificationDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyOtp(dto));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody SigninRequestDTO dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }
}