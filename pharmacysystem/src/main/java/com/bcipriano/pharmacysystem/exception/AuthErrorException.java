package com.bcipriano.pharmacysystem.exception;

public class AuthErrorException extends RuntimeException{

    public AuthErrorException(){
        super("Erro de autenticação.");
    }

    public AuthErrorException(String message) {
        super(message);
    }

}
