package br.edu.ifba.xpnewsbackend.infrastructure.exception;

public class UniqueViolationException extends RuntimeException{
    public UniqueViolationException(String message) {
        super(message);
    }
}
