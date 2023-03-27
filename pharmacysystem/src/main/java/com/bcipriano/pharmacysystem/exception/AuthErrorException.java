package com.bcipriano.pharmacysystem.exception;

public class AuthErrorException extends RuntimeException{

    public AuthErrorException(String message) {
        super(message);
    }

}
