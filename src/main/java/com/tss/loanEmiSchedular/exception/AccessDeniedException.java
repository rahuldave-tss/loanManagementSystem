package com.tss.loanEmiSchedular.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApplicationException{
    public AccessDeniedException(String message) {
        super(message,HttpStatus.FORBIDDEN);
    }
}
