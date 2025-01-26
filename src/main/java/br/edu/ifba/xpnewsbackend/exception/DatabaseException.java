package br.edu.ifba.xpnewsbackend.exception;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String message) {
        super(message);
    }
}
