package pl.first.firstjava;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LevelTest {

    private SudokuSolver testBacktrackingSudokuSolver = new BacktrackingSudokuSolver();
    private SudokuBoard testSudokuBoard = new SudokuBoard(testBacktrackingSudokuSolver);
    private Level testLevel;
    private int amount = 0;

    @Test
    void removeFieldsLOW() {
        testSudokuBoard.solveGame();
        testLevel = Level.LOW;
        testLevel.removeFields(testSudokuBoard);

        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < SudokuBoard.BOARD_SIZE; j++) {
                if(testSudokuBoard.get(i, j) == 0) {
                    amount++;
                }
            }
        }
        assertEquals(testLevel.getAmount(), amount);
    }

    @Test
    void removeFieldsMEDIUM() {
        testSudokuBoard.solveGame();
        testLevel = Level.MEDIUM;
        testLevel.removeFields(testSudokuBoard);

        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < SudokuBoard.BOARD_SIZE; j++) {
                if(testSudokuBoard.get(i, j) == 0) {
                    amount++;
                }
            }
        }
        assertEquals(testLevel.getAmount(), amount);
    }

    @Test
    void removeFieldsHARD() {
        testSudokuBoard.solveGame();
        testLevel = Level.HARD;
        testLevel.removeFields(testSudokuBoard);

        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < SudokuBoard.BOARD_SIZE; j++) {
                if(testSudokuBoard.get(i, j) == 0) {
                    amount++;
                }
            }
        }
        assertEquals(testLevel.getAmount(), amount);
    }

    @Test
    void values() {
        testLevel = Level.LOW;
        assertEquals(testLevel.getAmount(), 5);

        testLevel = Level.MEDIUM;
        assertEquals(testLevel.getAmount(), 10);

        testLevel = Level.HARD;
        assertEquals(testLevel.getAmount(), 15);
    }

}