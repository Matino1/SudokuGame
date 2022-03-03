package pl.first.firstjava.exceptions;

public class MySqlException extends SudokuApplicationException {
    public MySqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public MySqlException() {
        super();
    }
}
