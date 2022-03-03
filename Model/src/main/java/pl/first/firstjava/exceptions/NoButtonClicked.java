package pl.first.firstjava.exceptions;

public class NoButtonClicked extends SudokuApplicationException {

    public NoButtonClicked(String message, Throwable cause) {
        super(message, cause);
    }

    public NoButtonClicked() {
        super();
    }
}