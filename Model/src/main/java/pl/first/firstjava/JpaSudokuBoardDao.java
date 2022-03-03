package pl.first.firstjava;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pl.first.firstjava.exceptions.CantSaveFileException;
import pl.first.firstjava.exceptions.MySqlException;
import pl.first.firstjava.exceptions.NoFileException;

public class JpaSudokuBoardDao implements Dao<Save>, AutoCloseable {

    SessionFactory sessionFactory;

    public JpaSudokuBoardDao() {
        sessionFactory = new Configuration().configure()
                .buildSessionFactory();
    }

    @Override
    public List<Save> read() throws NoFileException, MySqlException {
        List<Save> saveList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            saveList = (List<Save>) session.createQuery("select s from save s").getResultList();

            transaction.commit();
            session.close();
        } catch (Exception e) {
            throw new MySqlException("Unable to read object from database", e);
        }
        return saveList;
    }


    // second parameter must be null or obj1 = obj2
    @Override
    public void write(Save obj1, Save obj2) throws CantSaveFileException, MySqlException {

        try (Session session = sessionFactory.openSession()) {

            if (obj1 != null) {
                Transaction transaction = session.beginTransaction();
                session.save(obj1);
                transaction.commit();
                session.close();
            }
        } catch (Exception e) {
            throw new MySqlException("Unable to save object in database", e);
        }
    }

    @Override
    public void deleteAll() throws MySqlException {

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Save> saveList = session
                    .createQuery("SELECT s FROM save s").getResultList();

            for (Save element : saveList) {
                session.remove(element);
            }
            transaction.commit();
            session.close();
        } catch (Exception e) {
            throw new MySqlException("Unable to delete all values from database", e);
        }
    }

    @Override
    public List<String> readNames() throws MySqlException {
        List<String> namesList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            namesList = (List<String>) session
                    .createQuery("select s.saveName from save s").getResultList();

            transaction.commit();
            session.close();
        } catch (Exception e) {
            throw new MySqlException("Unable to read saveNames from database", e);
        }
        return namesList;
    }

    @Override
    public List<String> readDate() throws MySqlException {
        List<String> dateList;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            dateList = (List<String>) session
                    .createQuery("select s.saveDate from save s").getResultList();

            transaction.commit();
            session.close();
        } catch (Exception e) {
            throw new MySqlException("Unable to read saveDate from database", e);
        }
        return dateList;
    }

    @Override
    public void close() throws Exception {
        sessionFactory.close();
    }
}
