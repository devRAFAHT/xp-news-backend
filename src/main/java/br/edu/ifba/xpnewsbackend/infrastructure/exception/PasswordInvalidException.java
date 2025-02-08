package br.edu.ifba.xpnewsbackend.infrastructure.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(String message) {
        super(message);
    }
}
