package com.tss.loanEmiSchedular.exception;

import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.asm.Advice;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException applicationException, HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setMessage(applicationException.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(httpServletRequest.getRequestURI());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,HttpServletRequest httpServletRequest) {

        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setPath(httpServletRequest.getRequestURI());

        //to get proper message
        String message = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getRequestURI());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
