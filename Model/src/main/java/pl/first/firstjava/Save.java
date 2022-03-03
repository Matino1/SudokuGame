package pl.first.firstjava;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity(name = "save")
@Table(name = "SAVE")
public class Save {

    @Id
    @GeneratedValue
    private int id;

    private final String saveName;
    private final String saveDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "saveId")
    private List<SudokuBoard> boards;

    public Save(String saveName, SudokuBoard modifiedBoard, SudokuBoard originalBoard) {
        this.saveName = saveName;
        this.saveDate = LocalDateTime.now().toString();
        boards = new ArrayList<>();
        addBoards(modifiedBoard, originalBoard);
    }

    public Save() {
        this.saveName = "save1";
        this.saveDate = LocalDateTime.now().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSaveName() {
        return saveName;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void addBoards(SudokuBoard board1, SudokuBoard board2) {
        if (board1 != null && board2 != null && boards.size() == 0) {
            boards.add(board1);
            boards.add(board2);
        }
    }

    public SudokuBoard getModifiedBoard() {
        return boards.get(0);
    }

    public SudokuBoard getOriginalBoard() {
        return boards.get(1);
    }

    public List<SudokuBoard> getBoards() {
        return boards;
    }

    public void setBoards(List<SudokuBoard> boards) {
        this.boards = boards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Save)) {
            return false;
        }

        Save save = (Save) o;

        return new EqualsBuilder()
                .append(saveName, save.saveName)
                .append(saveDate, save.saveDate)
                .append(boards, save.boards)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(saveName)
                .append(saveDate)
                .append(boards)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("saveName", saveName)
                .append("saveDate", saveDate)
                .append("boards", boards)
                .toString();
    }
}
