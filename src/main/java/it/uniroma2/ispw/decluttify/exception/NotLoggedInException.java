package it.uniroma2.ispw.decluttify.exception;

public class NotLoggedInException extends DecluttifyException {
    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
