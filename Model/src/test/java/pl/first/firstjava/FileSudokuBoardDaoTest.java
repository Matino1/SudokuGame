package pl.first.firstjava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import pl.first.firstjava.exceptions.NoFileException;

class FileSudokuBoardDaoTest {

    private final DaoFactory testDaoFactory = new DaoFactory();
    private final SudokuSolver testBacktrackingSudokuSolver = new BacktrackingSudokuSolver();
    private final SudokuBoard testSudokuBoard = new SudokuBoard(testBacktrackingSudokuSolver);
    private final SudokuBoard testSudokuBoard2 = new SudokuBoard(testBacktrackingSudokuSolver);


    @Test
    void read() {

        try(Dao<SudokuBoard> testFile = testDaoFactory.getFileDao("testFile.save")) {
            testSudokuBoard.solveGame();
            testSudokuBoard2.solveGame();
            testFile.write(testSudokuBoard, testSudokuBoard2);

            for(int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
                assertEquals(testFile.read().get(0).getRow(i), testSudokuBoard.getRow(i));
                assertEquals(testFile.read().get(1).getRow(i), testSudokuBoard2.getRow(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Dao<SudokuBoard> testFile = testDaoFactory.getFileDao("failFile.save");
        assertThrows(NoFileException.class, testFile::read);
    }

}
