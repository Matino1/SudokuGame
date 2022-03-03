package pl.first.firstjava;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import pl.first.firstjava.exceptions.CantSaveFileException;
import pl.first.firstjava.exceptions.MySqlException;
import pl.first.firstjava.exceptions.NoFileException;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    private final String fileName;

    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<SudokuBoard> read() throws NoFileException {
        ArrayList<SudokuBoard> listOfObjects = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            SudokuBoard firstObject = (SudokuBoard) ois.readObject();
            SudokuBoard secondObject = (SudokuBoard) ois.readObject();
            listOfObjects.add(firstObject);
            listOfObjects.add(secondObject);
            return listOfObjects;
        } catch (IOException | ClassNotFoundException ioe) {
            throw new NoFileException("unableToLoadGame", ioe);
        }
    }

    @Override
    public void write(SudokuBoard obj1, SudokuBoard obj2) throws CantSaveFileException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj1);
            oos.writeObject(obj2);
        } catch (IOException ioe) {
            throw new CantSaveFileException("UnableToSaveFile", ioe);
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<String> readNames() throws MySqlException {
        return null;
    }

    @Override
    public List<String> readDate() throws MySqlException {
        return null;
    }

    @Override
    public void close() {

    }

}
