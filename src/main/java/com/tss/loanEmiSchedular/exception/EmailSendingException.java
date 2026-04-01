package com.tss.loanEmiSchedular.exception;

import org.springframework.http.HttpStatus;

public class EmailSendingException extends ApplicationException{
    public EmailSendingException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
