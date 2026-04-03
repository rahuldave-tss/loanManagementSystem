package com.tss.loanEmiSchedular.service.impl;

import com.tss.loanEmiSchedular.dto.request.SigninRequestDTO;
import com.tss.loanEmiSchedular.dto.request.SignupRequestDTO;
import com.tss.loanEmiSchedular.dto.request.VerificationDto;
import com.tss.loanEmiSchedular.dto.response.AuthResponseDTO;
import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.Role;
import com.tss.loanEmiSchedular.events.SignupEvent;
import com.tss.loanEmiSchedular.exception.UserAlreadyExistsException;
import com.tss.loanEmiSchedular.exception.UserNotFoundException;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.service.AuthService;
import com.tss.loanEmiSchedular.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher applicationEventPublisher;
    private final JwtUtil jwtUtil;

    private final Map<String, SignupRequestDTO> data = new ConcurrentHashMap<>();
    private final Map<String, String> otpMap = new ConcurrentHashMap<>();
    private final Map<String, Long> expiry = new ConcurrentHashMap<>();



    //signup
    @Override
    public String signUp(SignupRequestDTO dto) {

        userRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("User already exists");
                });


        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // store temp
        data.put(dto.getEmail(), dto);
        otpMap.put(dto.getEmail(), otp);
        expiry.put(dto.getEmail(), System.currentTimeMillis() + 5 * 60 * 1000);

        // send email via event
        applicationEventPublisher.publishEvent(new SignupEvent(dto.getEmail(), otp));

        return "OTP sent to your email";
    }

    //signin
    @Override
    public AuthResponseDTO signIn(SigninRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO("Login successful", token);
    }

    public String verifyOtp(VerificationDto dto) {

        if (!expiry.containsKey(dto.getEmail()) || System.currentTimeMillis() > expiry.get(dto.getEmail())) {
            remove(dto.getEmail());
            return "OTP expired";
        }

        if (!otpMap.get(dto.getEmail()).equals(dto.getOtp())) {
            return "Invalid OTP";
        }

        SignupRequestDTO signUpdto = data.get(dto.getEmail());

        User user = new User();
        user.setEmail(signUpdto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpdto.getPassword()));
        user.setRole(signUpdto.getRole());
        user.setEmailVerified(true);

        userRepository.save(user);

        remove(dto.getEmail());

        return "User registered successfully";
    }

    private void remove(String email) {
        data.remove(email);
        otpMap.remove(email);
        expiry.remove(email);
    }

}
