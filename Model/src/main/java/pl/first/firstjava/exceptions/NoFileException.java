package pl.first.firstjava.exceptions;

public class NoFileException extends SudokuApplicationException {
    public NoFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
