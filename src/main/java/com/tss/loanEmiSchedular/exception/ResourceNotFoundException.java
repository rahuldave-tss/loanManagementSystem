package com.tss.loanEmiSchedular.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationException{
    public ResourceNotFoundException(String resource) {
        super(resource+" not found ", HttpStatus.NOT_FOUND);
    }
}
