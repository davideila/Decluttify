package it.uniroma2.ispw.decluttify.exception;

public class DecluttifyException extends RuntimeException {
    public DecluttifyException(String message) {
        super(message);
    }

    public DecluttifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
