package com.bcipriano.pharmacysystem.exception;

public class BusinessRuleException extends RuntimeException{

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException() {
        super("Campo inválido.");
    }

}
