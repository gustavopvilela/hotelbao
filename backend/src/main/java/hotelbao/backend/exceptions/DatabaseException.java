package hotelbao.backend.exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }
}