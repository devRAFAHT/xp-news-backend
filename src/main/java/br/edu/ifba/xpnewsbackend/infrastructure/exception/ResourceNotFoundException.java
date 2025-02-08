package br.edu.ifba.xpnewsbackend.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
