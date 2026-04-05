package com.tss.loanEmiSchedular.security;

import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.Role;
import com.tss.loanEmiSchedular.repository.UserRepository;
import com.tss.loanEmiSchedular.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null) {
            filterChain.doFilter(request, response); // next()
            return;
        }
        String token;

        if (header.startsWith("Bearer ")) {
            token = header.substring(7);
        } else {
            token = header; // accept raw token
        }
        try {
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            String path = request.getRequestURI();

            if(!user.isEmailVerified())
            {
                if(!path.startsWith("/api/v1/auth/verify"))
                {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Verify Your email First");
                    return;
                }

            }

            if (path.startsWith("/api/v1/borrower") &&
                    user.getRole() == Role.BORROWER &&
                    !user.isKycVerified()) {

                if (!path.startsWith("/api/v1/borrower/kyc")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("KYC not completed! First Complete KYC");
                    return;
                }
            }

            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(email, null, authorities)
            );

        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response); // next()
    }
}