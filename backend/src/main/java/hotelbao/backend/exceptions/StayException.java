package hotelbao.backend.exceptions;

public class StayException extends RuntimeException {
    public StayException() { super(); }

    public StayException(String message) { super(message); }
}
