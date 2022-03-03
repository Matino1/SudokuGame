package pl.first.firstjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BacktrackingSudokuSolverTest {

    @Test
    void solve() {
            int [][] testBoard1;
            int [][] testBoard2;
            SudokuSolver testBacktrackingSudokuSolver = new BacktrackingSudokuSolver();
            SudokuBoard testSudokuBoard = new SudokuBoard(testBacktrackingSudokuSolver);

            testSudokuBoard.solveGame();
            testBoard1 = testSudokuBoard.copyBoard();
            testSudokuBoard.solveGame();
            testBoard2 = testSudokuBoard.copyBoard();

            boolean compare = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (testBoard1[i][j] != testBoard2[i][j])
                        compare = false;
                }
            }
            assertFalse(compare);

    }
}