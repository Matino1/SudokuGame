package pl.first.firstjava;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SudokuFieldTest {

    private SudokuField testSudokuField = new SudokuField();

    @Test
    void setFieldValue() {
        testSudokuField.setValueInt(1);
        assertEquals(testSudokuField.getValueInt(),1);
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(SudokuField.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(testSudokuField.hashCode(),testSudokuField.hashCode());
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        SudokuField cloned = (SudokuField) testSudokuField.clone();
        assertTrue(testSudokuField.equals(cloned));
    }

    @Test
    void testCompareTo() {
        testSudokuField.setValueInt(2);
        SudokuField testSudokuFiled2 = new SudokuField();
        testSudokuFiled2.setValueInt(3);
        assertEquals(testSudokuFiled2.compareTo(testSudokuField), 1);
        assertThrows(NullPointerException.class,  () ->testSudokuField.compareTo(null));
    }

    @Test
    void testModifiable() {
        assertFalse(testSudokuField.isModifiable());
        testSudokuField.setModifiable(true);
        assertTrue(testSudokuField.isModifiable());
    }
}