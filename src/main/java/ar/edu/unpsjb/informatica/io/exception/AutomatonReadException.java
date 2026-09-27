package ar.edu.unpsjb.informatica.io.exception;

public class AutomatonReadException extends RuntimeException {
    public AutomatonReadException(String message) {
        super(message);
    }

    public AutomatonReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
