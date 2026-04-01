package com.tss.loanEmiSchedular.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApplicationException{
    public BusinessException(String message, HttpStatus status) {
        super(message,status);
    }
}
