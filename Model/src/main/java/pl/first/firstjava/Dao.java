package pl.first.firstjava;

import java.util.List;
import pl.first.firstjava.exceptions.CantSaveFileException;
import pl.first.firstjava.exceptions.MySqlException;
import pl.first.firstjava.exceptions.NoFileException;

public interface Dao<T> extends AutoCloseable {

     List<T> read() throws NoFileException, MySqlException;

     void write(T obj1, T obj2) throws CantSaveFileException, MySqlException;

     void deleteAll() throws MySqlException;

     List<String> readNames() throws MySqlException;

     List<String> readDate() throws MySqlException;
}
