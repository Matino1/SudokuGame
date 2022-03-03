package pl.first.firstjava;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuPart implements Serializable, Cloneable {

    private final List<SudokuField> fields;

    public SudokuPart() {
        this.fields = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            fields.set(i, new SudokuField());
        }
    }


    public List<SudokuField> getFields() {
        List<SudokuField> tmpField = Arrays.asList(new SudokuField[SudokuBoard.BOARD_SIZE]);
        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            tmpField.set(i, this.fields.get(i));
        }

        return tmpField;
    }

    public void setFields(List<SudokuField> fields) {
        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            this.fields.get(i).setValueInt(fields.get(i).getValueInt());
        }
    }

    public boolean verify() {
        boolean flag = false;
        for (int i = 1; i <= SudokuBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < SudokuBoard.BOARD_SIZE; j++) {
                if (this.fields.get(j).getValueInt() == i) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
            flag = false;
        }
        return true;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuPart)) {
            return false;
        }

        SudokuPart that = (SudokuPart) o;

        return new EqualsBuilder().append(fields, that.fields).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder(17, 37).append(fields).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fields", fields)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuPart part = new SudokuPart();
        List<SudokuField> list = new ArrayList<>();

        for (SudokuField element : fields) {
            list.add((SudokuField) element.clone());
        }
        part.setFields(list);
        return part;
    }

}
