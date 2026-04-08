package com.tss.loanEmiSchedular.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request,
            Map<String, String> errors
    ) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(message);
        error.setStatus(status.value());
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());
        error.setErrors(errors);

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException applicationException, HttpServletRequest httpServletRequest){
        log.error("Application Exception: "+applicationException);
        return buildErrorResponse(
                applicationException.getMessage(),
                applicationException.getStatus(),
                httpServletRequest,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        log.error("Method Argument Exception: "+ex);

        return buildErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                request,
                errors
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        log.error("Bad Credentials Exception: "+ex);
        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request,
                null
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnum(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        log.error("Json parse Exception: "+ex);
        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request,
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Generic Exception: "+ex);
        return buildErrorResponse(
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request,
                null
        );
    }

}
