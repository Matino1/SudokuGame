package pl.first.firstjava;

public class Repository {

    public SudokuBoard sudokuBoard;

    public Repository(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
    }

    public SudokuBoard createInstanceSudokuBoard() {
        SudokuBoard board = null;
        try {
            board = sudokuBoard.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return board;
    }
}
