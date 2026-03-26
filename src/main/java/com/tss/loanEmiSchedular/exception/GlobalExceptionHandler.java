package com.tss.loanEmiSchedular.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            UserNotFoundException.class,
            InvalidPasswordException.class,
            Exception.class
    })
    public ResponseEntity<ErrorResponse> handleAuthExceptions(
            RuntimeException ex,
            HttpServletRequest request) {

        HttpStatus status;

        if (ex instanceof UserAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof UserNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof  InvalidPasswordException){
            status = HttpStatus.UNAUTHORIZED;
        }
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                status.value(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, status);
    }
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), request.getRequestURI());
//
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
//            UserAlreadyExistsException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                ex.getMessage(),
//                HttpStatus.CONFLICT.value(),
//                request.getRequestURI()
//        );
//
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
//        ex.printStackTrace();
//        ErrorResponse error = new ErrorResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());
//
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}