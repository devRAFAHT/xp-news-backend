package br.edu.ifba.xpnewsbackend.infrastructure.exception;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String message) {
        super(message);
    }
}
