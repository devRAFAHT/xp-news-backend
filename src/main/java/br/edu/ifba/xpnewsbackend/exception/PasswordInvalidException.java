package br.edu.ifba.xpnewsbackend.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(String message) {
        super(message);
    }
}
