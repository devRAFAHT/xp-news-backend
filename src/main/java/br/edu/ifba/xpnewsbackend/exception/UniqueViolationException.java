package br.edu.ifba.xpnewsbackend.exception;

public class UniqueViolationException extends RuntimeException{
    public UniqueViolationException(String message) {
        super(message);
    }
}
