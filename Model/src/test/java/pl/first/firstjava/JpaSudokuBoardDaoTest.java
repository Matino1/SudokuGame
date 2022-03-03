package pl.first.firstjava;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import pl.first.firstjava.exceptions.CantSaveFileException;
import pl.first.firstjava.exceptions.MySqlException;
import pl.first.firstjava.exceptions.NoFileException;

class JpaSudokuBoardDaoTest {

    @Test
    void write() throws MySqlException, CantSaveFileException, NoFileException {
        SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        SudokuBoard sudokuBoard2 = new SudokuBoard(new BacktrackingSudokuSolver());

        sudokuBoard.solveGame();
        sudokuBoard2.solveGame();

        Dao<Save> JPAdao = new DaoFactory().getJpaDao();
        JPAdao.deleteAll();

        Save save = new Save("save1", sudokuBoard, sudokuBoard2);

        JPAdao.write(save, save);

        List<Save> list = JPAdao.read();

        SudokuBoard readSudokuBoard = JPAdao.read().get(0).getModifiedBoard();
        SudokuBoard readSudokuBoard2 = JPAdao.read().get(0).getOriginalBoard();

        for (int i = 0; i < SudokuBoard.BOARD_SIZE * SudokuBoard.BOARD_SIZE; i++) {
            assertEquals(readSudokuBoard.getByIndex(i), sudokuBoard.getByIndex(i));
            assertEquals(readSudokuBoard.getSudokuFieldsIsModifiable(i),sudokuBoard.getSudokuFieldsIsModifiable(i));
            assertEquals(readSudokuBoard2.getByIndex(i), sudokuBoard2.getByIndex(i));
            assertEquals(readSudokuBoard2.getSudokuFieldsIsModifiable(i),sudokuBoard2.getSudokuFieldsIsModifiable(i));
        }

        assertEquals(JPAdao.readNames().get(0), "save1");

        JPAdao.deleteAll();
    }
}