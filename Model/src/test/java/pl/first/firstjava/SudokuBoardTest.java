package pl.first.firstjava;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardTest {

    private SudokuSolver testBacktrackingSudokuSolver = new BacktrackingSudokuSolver();
    private SudokuBoard testSudokuBoard = new SudokuBoard(testBacktrackingSudokuSolver);

    @BeforeEach
    void beforeEach() {
        testSudokuBoard.solveGame();
    }

    @Test
    void set() {
        testSudokuBoard.set(0,0, 3);
        assertEquals(testSudokuBoard.get(0,0),3);
    }

    @Test
    void checkBoard() {
        assertTrue(testSudokuBoard.checkBoard());
        testSudokuBoard.setByIndex(0,10);
        assertFalse(testSudokuBoard.checkBoard());
    }

    @Test
    void solveGame() {
        boolean isCorect = false;
        for (int i = 0; i < 9; i++) {
            if ((testSudokuBoard.getRow(i).verify() || testSudokuBoard.getColumn(i).verify())) {
                isCorect = false;
            }
            else {
                isCorect = true;
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j += 3) {
                if (!(testSudokuBoard.getBox(i, j).verify())) {
                    isCorect = false;
                } else {
                    isCorect = true;
                }
            }
        }
        assertTrue(isCorect);
    }

    @Test
    void getSudokuField() {
       assertEquals(testSudokuBoard.getSudokuField(0), testSudokuBoard.getSudokuField(0));
    }

    @Test
    void setByIndex() {
        testSudokuBoard.setByIndex(0,1);
        assertEquals(testSudokuBoard.getByIndex(0), 1);
    }

    @Test
    void getRow() {
        boolean flag = true;

        for (int i = 0; i < 9; i++) {
            if (testSudokuBoard.getRow(2).getFields().get(i).getValueInt() != testSudokuBoard.get(i,2)) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    void getColumn() {
        boolean flag = true;

        for (int i = 0; i < 9; i++) {
            if (testSudokuBoard.getColumn(2).getFields().get(i).getValueInt() != testSudokuBoard.get(2,i)) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    void getBox() {
        boolean flag = true;
        int k = 0;

        for (int i = 3; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if (testSudokuBoard.getBox(3,0).getFields().get(k++).getValueInt() != testSudokuBoard.get(j,i)) {
                    flag = false;
                    break;
                }
            }
        }
        assertTrue(flag);
    }

    @Test
    void checkRow() {
        assertTrue(testSudokuBoard.checkRow());

        testSudokuBoard.set(0,0,0);
        assertFalse(testSudokuBoard.checkRow());
        testSudokuBoard.set(8,8,10);
        assertFalse(testSudokuBoard.checkRow());
    }

    @Test
    void checkColumn() {
        assertTrue(testSudokuBoard.checkColumn());

        testSudokuBoard.set(0,0,0);
        assertFalse(testSudokuBoard.checkColumn());
        testSudokuBoard.set(8,8,10);
        assertFalse(testSudokuBoard.checkColumn());
    }

    @Test
    void checkBox() {
        assertTrue(testSudokuBoard.checkBox());

        testSudokuBoard.set(0,0,0);
        assertFalse(testSudokuBoard.checkBox());
        testSudokuBoard.set(8,8,10);
        assertFalse(testSudokuBoard.checkBox());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(SudokuBoard.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(testSudokuBoard.hashCode(),testSudokuBoard.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(testSudokuBoard.toString(),testSudokuBoard.toString());
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        SudokuBoard clonedBoard = testSudokuBoard.clone();

        for(int i = 0; i < SudokuBoard.BOARD_SIZE * SudokuBoard.BOARD_SIZE; i++) {
            assertEquals(clonedBoard.getByIndex(i), testSudokuBoard.getByIndex(i));
        }
        testSudokuBoard.set(0,0,0);

        assertNotEquals(clonedBoard.getRow(0), testSudokuBoard.getRow(0));
        assertNotSame(clonedBoard, testSudokuBoard);
    }

    @Test
    void testModifiableFields() throws CloneNotSupportedException {
        assertFalse(testSudokuBoard.getSudokuFieldsIsModifiable(0));
        testSudokuBoard.setSudokuFieldModifiable(0, true);
        assertTrue(testSudokuBoard.getSudokuFieldsIsModifiable(0));
    }

}
