package com.shopping.exception;

import org.springframework.http.HttpStatus;

public class QuantityLimitExceededException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public QuantityLimitExceededException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
