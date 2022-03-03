package pl.first.firstjava;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SudokuPartTest {

    private  List<SudokuField> testSudokuFields = Arrays.asList(new SudokuField[9]);
    private  SudokuPart testSudokuPart = new SudokuPart();

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 9; i++) {
            testSudokuFields.set(i, new SudokuField());
        }
    }

    @Test
    void setFields() {
        testSudokuFields.get(0).setValueInt(5);
        testSudokuFields.get(1).setValueInt(6);
        testSudokuPart.setFields(testSudokuFields);

        assertEquals(testSudokuFields.get(0).getValueInt() , testSudokuPart.getFields().get(0).getValueInt());
        assertEquals(testSudokuFields.get(1).getValueInt() , testSudokuPart.getFields().get(1).getValueInt());
    }

    @Test
    void verify() {
        for (int i = 0; i < 9; i++) {
            testSudokuFields.get(i).setValueInt(i+1);
        }

        testSudokuPart.setFields(testSudokuFields);
        assertEquals(testSudokuPart.verify(), true);

        testSudokuFields.get(8).setValueInt(5);
        testSudokuPart.setFields(testSudokuFields);
        assertEquals(testSudokuPart.verify(), false);
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(SudokuPart.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(testSudokuPart.hashCode(),testSudokuPart.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(testSudokuPart.toString(),testSudokuPart.toString());
    }

    @Test
    void testClone() {
        testSudokuFields.get(0).setValueInt(5);
        testSudokuFields.get(1).setValueInt(6);
        testSudokuPart.setFields(testSudokuFields);
        SudokuPart testCloned = new SudokuPart();
        try {
            testCloned = (SudokuPart) testSudokuPart.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < SudokuBoard.BOARD_SIZE / 9; i++) {
            assertEquals(testSudokuPart.getFields().get(i).getValueInt(), testCloned.getFields().get(i).getValueInt());
        }

        assertNotSame(testSudokuPart.getFields(), testCloned.getFields());
    }
}