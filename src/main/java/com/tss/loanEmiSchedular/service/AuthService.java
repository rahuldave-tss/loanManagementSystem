package com.tss.loanEmiSchedular.service;

import com.tss.loanEmiSchedular.config.SecurityConfig;
import com.tss.loanEmiSchedular.dto.request.SigninRequestDTO;
import com.tss.loanEmiSchedular.dto.request.SignupRequestDTO;
import com.tss.loanEmiSchedular.dto.response.AuthResponseDTO;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.Role;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //signup
    public String signUp(SignupRequestDTO dto)
    {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("User already exists");
                });

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.BORROWER);

        userRepository.save(user);
        return "User registered successfully";


    }

    //signin
    public AuthResponseDTO signIn(SigninRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO("Login successful", token);
    }

}
