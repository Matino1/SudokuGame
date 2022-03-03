package pl.comp.viewmodule;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.first.firstjava.Authors;
import pl.first.firstjava.Dao;
import pl.first.firstjava.DaoFactory;
import pl.first.firstjava.Save;
import pl.first.firstjava.exceptions.MySqlException;
import pl.first.firstjava.exceptions.NoFileException;

public class MenuController {

    private int levelFlag;
    private final Logger log = LogManager.getLogger(MenuController.class.getName());
    private final Stage stage;
    private final Scene scene;
    private String loadFilePath;
    private String saveNameInDB;
    Locale locale;
    private final ResourceBundle bundle;

    @FXML
    private Button easyLevelButton;

    @FXML
    private Button mediumLevelButton;

    @FXML
    private Button hardLevelButton;

    @FXML
    private Button buttonLoadBoard;

    @FXML
    private Button buttonChangeLanguage;

    @FXML
    private Label authorsLabel;

    @FXML
    private void initialize() {
        easyLevelButton.setBackground(new Background(new BackgroundFill(Color
                .rgb(0,0,0), null, null)));
        mediumLevelButton.setBackground(new Background(new BackgroundFill(Color
                .rgb(0,0,0), null, null)));
        hardLevelButton.setBackground(new Background(new BackgroundFill(Color
                .rgb(0,0,0), null, null)));

        buttonLoadBoard.setOnAction(actionEvent -> loadGameAction());
        easyLevelButton.setOnAction(actionEvent -> openBoard(1));
        mediumLevelButton.setOnAction(actionEvent -> openBoard(2));
        hardLevelButton.setOnAction(actionEvent -> openBoard(3));

        buttonChangeLanguage.setOnAction(actionEvent -> changeLanguageDialogConfirmation());

        Authors authors = new Authors();
        authorsLabel.setText(authors.getString("author1"));
    }

    public MenuController(String languageCode) throws NoFileException {
        stage = new Stage();
        changeLanguage(languageCode);
        bundle = ResourceBundle.getBundle("bundles.messages", locale);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            loader.setController(this);
            loader.setResources(bundle);
            scene = new Scene(loader.load(), 800, 600);

            String css = Objects.requireNonNull(this.getClass()
                    .getResource("MenuControllerStyle.css")).toExternalForm();
            scene.getStylesheets().add(css);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(bundle.getString("applicationTitle"));
        } catch (IOException e) {
            NoFileException exception = new NoFileException(bundle.getString("noFileException"), e);
            log.error(bundle.getString("logExceptionDescription"), exception);
            throw exception;
        }
    }


    public Scene getScene() {
        return scene;
    }

    public void setLevelFlag(int levelFlag) {
        this.levelFlag = levelFlag;
    }

    public int getLevelFlag() {
        return levelFlag;
    }

    public String getSaveNameInDB() {
        return saveNameInDB;
    }

    public String getLoadFilePath() {
        return loadFilePath;
    }

    public Stage getStage() {
        return stage;
    }

    public Locale getLocale() {
        return locale;
    }


    public void showStage() {
        stage.show();
    }

    public void loadGameAction() {
        Dao<Save> daoFactory = new DaoFactory().getJpaDao();
        VBox saveBox = new VBox();
        List<String> namesList = null;
        List<String> dateList = null;
        try {
            namesList = daoFactory.readNames();
            dateList = daoFactory.readDate();
        } catch (MySqlException e) {
            log.error(bundle.getString(e.getLocalizedMessage()));
        }

        if (namesList != null && dateList != null) {
            Collections.reverse(namesList);
            Collections.reverse(dateList);

            int i = 0;
            for (String element : namesList) {
                LocalDateTime dateTime = LocalDateTime.parse(dateList.get(i));
                Label elementLabel = new Label(element);
                Label dateLabel = new Label("Data zapisu: " + dateTime
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                elementLabel.setMinSize(190, 35);
                dateLabel.setMinSize(180, 35);

                HBox heightBox = new HBox(elementLabel, dateLabel);
                heightBox.setStyle("-fx-border-color: black;"
                        + "-fx-padding: 5px");
                saveBox.getChildren().add(heightBox);
                i++;
                heightBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        saveNameInDB = element;
                        openBoard(5);
                    }
                });
            }
        } else {
            saveBox.getChildren().add(new Label(bundle.getString("saveNoExist")));
        }



        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(saveBox);
        Button cancelButton = new Button("Anuluj");
        cancelButton.setOnAction(actionEvent ->  this.getStage().setScene(this.getScene()));

        Button loadFromFileButton = new Button("Load from file");
        loadFromFileButton.setOnAction(actionEvent -> loadFile());

        HBox heightBoxActionButtons = new HBox(10,cancelButton, loadFromFileButton);
        heightBoxActionButtons.setStyle("-fx-padding: 5px");
        heightBoxActionButtons.setMinSize(400,40);

        Label titleLabel = new Label("Wybierz zapis który chesz wczytać");
        titleLabel.setMinSize(390, 40);
        titleLabel.setStyle("-fx-alignment: center");

        VBox box = new VBox(titleLabel, scrollPane, heightBoxActionButtons);

        Scene scene = new Scene(box, 400,400);
        stage.setScene(scene);
        stage.show();
    }

    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("loadGameButtonLabel"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".save", "*.save"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            loadFilePath = file.getAbsolutePath();
            openBoard(4);
        }
    }

    public void changeLanguage(String languageCode) {
        if (languageCode.length() == 2) {
            this.locale = new Locale(languageCode);
        }
    }

    public void changeLanguageDialogConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UTILITY);
        alert.setGraphic(null);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #DAA520; "
                + "-fx-text-fill: red; "
                + "-fx-border-color: white; "
                + "-fx-border-width: 5px;"
                + "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;");
        alert.setContentText(bundle.getString("selectLanguage"));

        ButtonType polishButton = new ButtonType(bundle.getString("polishLabel"));
        ButtonType englishButton = new ButtonType((bundle.getString("englishLabel")));
        ButtonType cancelButton =
                new ButtonType((bundle.getString("cancelLabel")),
                        ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(polishButton, englishButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == polishButton) {
            changeLanguage("pl");

            try {
                MenuController menuController = new MenuController("pl");
                menuController.showStage();
            } catch (NoFileException e) {
                log.error(e.getLocalizedMessage());
            }
            log.info("Zmienono język na polski");
            stage.close();
        } else if (result.get() == englishButton) {
            changeLanguage("en");
            try {
                MenuController menuController = new MenuController("en");
                menuController.showStage();

            } catch (NoFileException e) {
                log.error(e.getLocalizedMessage());
            }
            log.info("The language was changed to english");
            stage.close();
        }

    }

    public void openBoard(final int levelFlag) {
        setLevelFlag(levelFlag);
        try {
            BoardController boardController = new BoardController(this);
        } catch (NoFileException e) {
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}