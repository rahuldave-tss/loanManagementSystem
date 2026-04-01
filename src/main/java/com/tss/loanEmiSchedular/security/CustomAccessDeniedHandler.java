package com.tss.loanEmiSchedular.security;


import com.tss.loanEmiSchedular.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException ex)
            throws IOException {
        ErrorResponse error = new ErrorResponse(
                "You do not have permission to perform this action",
                HttpServletResponse.SC_FORBIDDEN,
                request.getRequestURI()
        );

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}