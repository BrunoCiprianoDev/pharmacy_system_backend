package com.bcipriano.pharmacysystem.exception;

public class InvalidIdException extends RuntimeException{

    public InvalidIdException() {
        super("Invalid ID");
    }

    public InvalidIdException(String message) {
        super(message);
    }

}
