package it.uniroma2.ispw.decluttify.exception;

public class SessionExpiredException extends DecluttifyException {
    public SessionExpiredException(String message) {
        super(message);
    }
    public SessionExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
