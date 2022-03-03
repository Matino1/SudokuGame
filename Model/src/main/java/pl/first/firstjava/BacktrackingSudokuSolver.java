package pl.first.firstjava;

import java.io.Serializable;

public class BacktrackingSudokuSolver implements SudokuSolver, Serializable {

    private boolean isInRow(int row, int number, SudokuBoard board) {
        for (int i = 0; i < 9; i++) {
            if (board.get(row,i) == number) {
                return true;
            }
        }
        return false;
    }

    private boolean isInColumn(int column, int number, SudokuBoard board) {
        for (int i = 0; i < 9; i++) {
            if (board.get(i,column) == number) {
                return true;
            }
        }
        return false;
    }

    private boolean isInField(int row, int column, int number, SudokuBoard board) {
        int rowInField = row - row % 3;
        int columnInField = column - column % 3;
        for (int i = rowInField; i < rowInField + 3; i++) {
            for (int j = columnInField; j < columnInField + 3; j++) {
                if (board.get(i,j) == number) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSafe(int row, int column, int number, SudokuBoard board) {
        if (isInRow(row, number, board) || isInColumn(column, number, board)
                || isInField(row, column, number, board)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean solve(SudokuBoard board) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board.get(row,column) == 0) {
                    for (int number = 1; number <= 9; number++) {
                        if (isSafe(row, column, number, board)) {
                            board.set(row, column, number);
                            if (solve(board)) {
                                return true;
                            } else {
                                board.set(row, column, 0);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
