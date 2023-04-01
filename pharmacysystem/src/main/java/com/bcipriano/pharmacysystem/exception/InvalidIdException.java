package com.bcipriano.pharmacysystem.exception;

public class InvalidIdException extends RuntimeException{

    public InvalidIdException() {
        super("Id inválido.");
    }

    public InvalidIdException(String message) {
        super(message);
    }

}
