package it.uniroma2.ispw.decluttify.exception;

public class ConfigurationException extends DecluttifyException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
