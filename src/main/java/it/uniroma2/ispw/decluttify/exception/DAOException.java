package it.uniroma2.ispw.decluttify.exception;

public class DAOException extends DecluttifyException {
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
