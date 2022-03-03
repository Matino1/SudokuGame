package pl.first.firstjava.exceptions;

public class CantLoadFileException extends SudokuApplicationException {

    public CantLoadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantLoadFileException() {
        super();
    }
}