package com.bcipriano.pharmacysystem.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public BusinessRuleException() {
        super("Invalid attribute.");
    }

    public BusinessRuleException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
