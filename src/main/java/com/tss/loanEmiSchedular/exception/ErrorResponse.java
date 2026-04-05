package com.tss.loanEmiSchedular.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    private Map<String,String> errors;

    public ErrorResponse(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}