package pl.comp.viewmodule;

import java.io.IOException;
import javafx.stage.Stage;
import pl.first.firstjava.exceptions.NoFileException;

public class BoardApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        MenuController menuController = null;
        try {
            menuController = new MenuController("pl");
        } catch (NoFileException e) {
            e.printStackTrace();
        }
        menuController.showStage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}