package pl.first.firstjava;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RepositoryTest {

    @Test
    void createInstanceSudokuBoard() {
        SudokuBoard testSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        Repository testRepo = new Repository(testSudokuBoard);

        assertSame(testSudokuBoard, testRepo.sudokuBoard);
        assertNotSame(testSudokuBoard, testRepo.createInstanceSudokuBoard());
    }
}