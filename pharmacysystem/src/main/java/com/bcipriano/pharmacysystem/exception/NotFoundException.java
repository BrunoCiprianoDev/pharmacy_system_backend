package com.bcipriano.pharmacysystem.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(){
        super("Objeto não encontrado.");
    }

    public NotFoundException(String message) {
        super(message);
    }

}
