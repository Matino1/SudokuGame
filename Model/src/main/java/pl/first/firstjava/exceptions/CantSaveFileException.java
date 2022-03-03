package pl.first.firstjava.exceptions;

public class CantSaveFileException extends SudokuApplicationException {

    public CantSaveFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantSaveFileException() {
        super();
    }
}