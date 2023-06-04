package com.bcipriano.pharmacysystem.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(){
        super("Autenticação inválida!");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
