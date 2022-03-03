package pl.first.firstjava;

public class DaoFactory {

    public Dao<SudokuBoard> getFileDao(String fileName) {
        FileSudokuBoardDao fileSudokuBoardDao = new FileSudokuBoardDao(fileName);
        return fileSudokuBoardDao;
    }

    public Dao<Save> getJpaDao() {
        JpaSudokuBoardDao jpaSudokuBoardDao = new JpaSudokuBoardDao();
        return jpaSudokuBoardDao;
    }

}
