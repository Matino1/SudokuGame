package pl.first.firstjava;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity(name = "sudokuField")
@Table(name = "SUDOKUFIELD")
public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int valueInt;
    private boolean isModifiable;

    @Transient
    private transient IntegerProperty valueProperty;

    public SudokuField() {
        this.valueInt = 0;
        this.valueProperty = new SimpleIntegerProperty(0);
        this.isModifiable = false;

        valueProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number>
                                        observableValue, Number number, Number t1) {
                valueInt = valueProperty.getValue();
            }
        });
    }
    
    public int getValueInt() {
        return valueInt;
    }

    public IntegerProperty getProperty() {
        return valueProperty;
    }

    public void setValueInt(int valueInt) {
            this.valueProperty.setValue(valueInt);
    }

    public void updateProperty() {
        this.valueProperty = new SimpleIntegerProperty();
        this.setValueInt(this.getValueInt());
    }

    public boolean isModifiable() {
        return isModifiable;
    }

    public void setModifiable(boolean modifiable) {
        isModifiable = modifiable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuField)) {
            return false;
        }

        SudokuField field = (SudokuField) o;

        return new EqualsBuilder()
                .append(valueInt, field.valueInt)
                .append(isModifiable, field.isModifiable)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(valueInt)
                .append(isModifiable)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("valueInt", valueInt)
                .append("isModifiable", isModifiable)
                .append("valueProperty", valueProperty)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(SudokuField o) throws NullPointerException {
        try {
            return Integer.compare(this.getValueInt(), o.getValueInt());
        } catch (NullPointerException npe) {
            throw new NullPointerException("Compared object is null");
        }
    }
}
