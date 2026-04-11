package it.uniroma2.ispw.decluttify.exception;

public class ModelException extends DecluttifyException {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }
}