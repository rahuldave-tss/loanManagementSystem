package com.tss.loanEmiSchedular.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(ApplicationException applicationException, HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setMessage(applicationException.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(httpServletRequest.getRequestURI());
        errorResponse.setStatus(applicationException.getStatus().value());

        return ResponseEntity.status(applicationException.getStatus().value()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Validation failed");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(errors);

        return ResponseEntity.badRequest().body(errorResponse);
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

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGenericException(
//            Exception ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse();
//        error.setMessage("Something went wrong");
//        error.setTimestamp(LocalDateTime.now());
//        error.setPath(request.getRequestURI());
//        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

}
