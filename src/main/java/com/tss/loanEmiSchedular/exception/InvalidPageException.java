package com.tss.loanEmiSchedular.exception;

import org.springframework.http.HttpStatus;

public class InvalidPageException extends ApplicationException{
    public InvalidPageException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
