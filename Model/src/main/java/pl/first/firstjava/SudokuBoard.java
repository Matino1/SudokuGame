package pl.first.firstjava;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity(name = "sudokuBoard")
@Table(name = "SUDOKUBOARD")
public class SudokuBoard implements Serializable, Cloneable {

    public static final int BOARD_SIZE = 9;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sudokuBoardId")
    private final List<SudokuField> board = Arrays.asList(new SudokuField[BOARD_SIZE * BOARD_SIZE]);

    @Transient
    private final SudokuSolver sudokuSolver;

    public SudokuBoard(SudokuSolver sudokuSolver) {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
                board.set(i, new SudokuField());
            }
        this.sudokuSolver = sudokuSolver;
    }


    public SudokuBoard() {
        this.sudokuSolver = new BacktrackingSudokuSolver();

    }

    public long getSudokuBoardId() {
        return id;
    }

    public void setSudokuBoardId(long sudokuBoardId) {
        this.id = sudokuBoardId;
    }

    public void setSudokuFieldModifiable(int index, boolean isModifiable) {
        board.get(index).setModifiable(isModifiable);
    }

    public boolean getSudokuFieldsIsModifiable(int index) {
        return board.get(index).isModifiable();
    }

    public void solveGame() {
        for (int i = 0; i < 9; i++) {
            board.get(i).setValueInt(i + 1);
        }
        Collections.shuffle(board);
        sudokuSolver.solve(this);
    }

    public int get(int x, int y) {
        return board.get(x + y * 9).getValueInt();
    }

    public void set(int x, int y, int value) {
        this.board.get(x + y * 9).setValueInt(value);
    }

    public int getByIndex(int index) {
        return board.get(index).getValueInt();
    }

    public void setByIndex(int index, int value) {
        this.board.get(index).setValueInt(value);
    }

    public IntegerProperty getFieldProperty(int index) {
        return board.get(index).getProperty();
    }

    public SudokuField getSudokuField(int index) {
        return board.get(index);
    }

    public void updateFieldsProperty() {
        for (SudokuField element : board) {
            element.updateProperty();
        }
    }

    public int [][] copyBoard() {
        int [][] copyBoard = new int[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                copyBoard[i][j] = board.get(i * 9 + j).getValueInt();
            }
        }
        return copyBoard;
    }

    public SudokuRow getRow(int y) {
        SudokuRow row = new SudokuRow();
        List<SudokuField> tmpTab = Arrays.asList(new SudokuField[9]);

        for (int i = 0; i < BOARD_SIZE; i++) {
            tmpTab.set(i, board.get(y * 9 + i));
        }
        row.setFields(tmpTab);
        return row;
    }

    public SudokuColumn getColumn(int x) {
        SudokuColumn column = new SudokuColumn();
        List<SudokuField> tmpTab;
        tmpTab = Arrays.asList(new SudokuField[9]);

        for (int i = 0; i < BOARD_SIZE; i++) {
            tmpTab.set(i, board.get(x + i * 9));
        }
        column.setFields(tmpTab);
        return column;
    }

    public SudokuBox getBox(int x, int y) {
        SudokuBox box = new SudokuBox();
        List<SudokuField> tmpTab = Arrays.asList(new SudokuField[9]);

        int rowInField = x - x % 3;
        int columnInField = y - y % 3;
        int k = 0;
        for (int i = rowInField; i < rowInField + 3; i++) {
            for (int j = columnInField; j < columnInField + 3; j++) {
                tmpTab.set(k++, board.get(i * 9 + j));
            }
        }
        box.setFields(tmpTab);
        return box;
    }

        public boolean checkBoard() {
            return checkBox() && checkRow() && checkColumn();
        }

    public boolean checkRow() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!(getRow(i).verify())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkColumn() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!(getColumn(i).verify())) {
                return false;
            }
        }
        return true;
    }

    public boolean checkBox() {
        for (int i = 0; i < BOARD_SIZE; i += 3) {
            for (int j = 0; j < BOARD_SIZE; j += 3) {
                if (!(getBox(i,j).verify())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuBoard)) {
            return false;
        }

        SudokuBoard that = (SudokuBoard) o;

        return new EqualsBuilder()
                .append(board, that.board)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(board)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("board", board)
                .append("sudokuSolver", sudokuSolver)
                .toString();
    }

    @Override
    public SudokuBoard clone() throws CloneNotSupportedException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);

        //SudokuBoard board = new SudokuBoard(this.sudokuSolver);

        List<SudokuField> fields = List.copyOf(this.board);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board.set(i, j, fields.get(i + j * 9).getValueInt());
                if (this.board.get(i + j * 9).isModifiable()) {
                    board.setSudokuFieldModifiable(i + j * 9, true);
                }
            }
        }
        return board;
    }

}


