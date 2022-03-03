package pl.comp.viewmodule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.first.firstjava.BacktrackingSudokuSolver;
import pl.first.firstjava.Dao;
import pl.first.firstjava.DaoFactory;
import pl.first.firstjava.Level;
import pl.first.firstjava.Repository;
import pl.first.firstjava.Save;
import pl.first.firstjava.SudokuBoard;
import pl.first.firstjava.exceptions.CantSaveFileException;
import pl.first.firstjava.exceptions.MySqlException;
import pl.first.firstjava.exceptions.NoButtonClicked;
import pl.first.firstjava.exceptions.NoFileException;


public class BoardController {

    private SudokuBoard sudokuBoard;
    private final Logger log = LogManager.getLogger(BoardController.class.getName());
    private Repository repository;
    private final MenuController menuController;
    private final Stage stage;
    private Button clickedSudokuFieldButton;
    private ResourceBundle bundle;
    private final Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    private GridPane boardTable;

    @FXML
    private Button buttonCheckAnswer;

    @FXML
    private Label resultLabel;

    @FXML
    private Button buttonChangeLevel;

    @FXML
    private Button buttonNumber1;

    @FXML
    private Button buttonNumber2;

    @FXML
    private Button buttonNumber3;

    @FXML
    private Button buttonNumber4;

    @FXML
    private Button buttonNumber5;

    @FXML
    private Button buttonNumber6;

    @FXML
    private Button buttonNumber7;

    @FXML
    private Button buttonNumber8;

    @FXML
    private Button buttonNumber9;

    @FXML
    private Button buttonSaveBoard;

    @FXML
    private Label timeLabel;

    @FXML
    private void initialize() {
        this.timer();
        if (menuController.getLevelFlag() == 4) {
           loadGameFromFile(menuController.getLoadFilePath());
        } else if (menuController.getLevelFlag() == 5) {
            loadGameFromDb(menuController.getSaveNameInDB());
        } else {
            createGameLevel(menuController.getLevelFlag());
        }
        showSudokuBoardInScene();

        buttonChangeLevel.setOnAction(actionEvent -> changeLevel());
        buttonCheckAnswer.setOnAction(actionEvent -> checkBoardClick());
        buttonSaveBoard.setOnAction(actionEvent -> {
            try {
                saveGameAction();
            } catch (MySqlException e) {
                log.error(bundle.getString(e.getLocalizedMessage()));
            }
        });


        this.insertButtonsInitialize();
        this.alertWindowConfiguration();
    }

    public BoardController(MenuController menuController) throws NoFileException {
        this.menuController = menuController;
        stage = menuController.getStage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("board.fxml"));
            loader.setController(this);
            bundle = ResourceBundle.getBundle("bundles.messages", menuController.getLocale());
            loader.setResources(bundle);
            Scene scene = new Scene(loader.load(), 800, 600);

            String css = this.getClass().getResource("BoardControllerStyle.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(bundle.getString("applicationTitle"));
        } catch (IOException e) {
            throw new NoFileException(bundle.getString("noFileException"), e);
        }
    }

    private Button createButton(String label, int id) {
        Button button = new Button(label);
        button.setId(String.valueOf(id));
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setClickedButtonFromGridPane(button);
            }
        });

        return button;
    }

    public void insertButtonsInitialize() {
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(buttonNumber1);
        buttonList.add(buttonNumber2);
        buttonList.add(buttonNumber3);
        buttonList.add(buttonNumber4);
        buttonList.add(buttonNumber5);
        buttonList.add(buttonNumber6);
        buttonList.add(buttonNumber7);
        buttonList.add(buttonNumber8);
        buttonList.add(buttonNumber9);

        for (Button btn : buttonList) {
            btn.setOnAction(actionEvent -> {
                try {
                    changeValueOfSudokuBoardByButton(btn);
                } catch (NoButtonClicked e) {
                    log.error(e.getLocalizedMessage());
                    alert.setContentText(bundle.getString("alertNoSelectedField"));
                    alert.showAndWait();
                }
            });
        }
    }

    public void showSudokuBoardInScene() {
        int idCounter;
        Button button;
        StringConverter<Number> converter = new NumberStringConverter();

        for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
            for (int j = 0; j < SudokuBoard.BOARD_SIZE; j++) {
                idCounter = i + j * 9;
                if (this.sudokuBoard.get(i, j) != 0) {
                    button = (createButton(String.valueOf(this.sudokuBoard.get(i, j)), idCounter));
                    button.setFont(new Font("Coryntia", 24));
                    button.setDisable(true);

                    if (sudokuBoard.getSudokuFieldsIsModifiable(i + j * 9)) {
                        button.setDisable(false);
                    }
                } else {
                    button = createButton("", idCounter);
                    sudokuBoard.setSudokuFieldModifiable(i + j * 9, true);
                    button.setFont(Font.font(0));
                }
                boardTable.add(button, i, j);
                //JavaBeanIntegerProperty sudokuFieldProperty = JavaBeanIntegerPropertyBuilder
                // .create().bean(sudokuBoard.getSudokuField(i + j * 9)).name("value").build();
                Bindings.bindBidirectional(button.textProperty(),
                        sudokuBoard.getFieldProperty(i + j * 9), converter);
            }
        }
    }

    public void createGameLevel(int flag) {
        repository = new Repository(new SudokuBoard(new BacktrackingSudokuSolver()));
        repository.sudokuBoard.solveGame();
        sudokuBoard = repository.createInstanceSudokuBoard();
        Level level = null;

        if (flag == 1) {
            level = Level.LOW;
            log.info(bundle.getString("logEasyLevelLoaded"));
        }
        if (flag == 2) {
            level = Level.MEDIUM;
            log.info(bundle.getString("mediumLevelLoaded"));
        }
        if (flag == 3) {
            level = Level.HARD;
            log.info(bundle.getString("hardLevelLoaded"));
        }
        if (level != null) {
            level.removeFields(sudokuBoard);
        }
    }

    public void timer() {

        timeLabel.setText("00:00");
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000),
                new EventHandler<ActionEvent>() {

            int mins = 0;
            int secs = 1;

            @Override
            public void handle(ActionEvent event) {
                if (mins >= 60) {
                    timeLabel.setFont(Font.font(15));
                    timeLabel.setText(bundle.getString("tooMuchTime"));
                } else {
                    if (secs == 60) {
                        mins++;
                        secs = 0;
                    }
                    timeLabel.setText((((mins / 10) == 0) ? "0" : "") + mins + ":"
                            + (((secs / 10) == 0) ? "0" : "") + secs++); // + ":"
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();
    }

    public void alertWindowConfiguration() {
        alert.setTitle(bundle.getString("warning"));
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UTILITY);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #DAA520; "
                + "-fx-text-fill: red; "
                + "-fx-border-color: white; "
                + "-fx-border-width: 5px;"
                + "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;");
    }

    public void setClickedButtonFromGridPane(Button button) {
        if (button != null) {
            clickedSudokuFieldButton = button;

            clickedSudokuFieldButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    changeValueOfSudokuBoardByKey(keyEvent);
                }
            });
        }
    }

    public void changeValueOfSudokuBoardByKey(KeyEvent keyEvent) {
        try {
            int keyCharInt = Integer.parseInt(keyEvent.getText());

            if (keyCharInt >= 1 && keyCharInt <= 9) {
                sudokuBoard.setByIndex(Integer
                        .parseInt(clickedSudokuFieldButton.getId()), keyCharInt);
                clickedSudokuFieldButton.setFont(new Font("Coryntia", 24));
            }
        }   catch (Exception e) {
            log.warn("invalidKey");
        }


    }

    public void changeValueOfSudokuBoardByButton(Button button) throws NoButtonClicked {
       try {
           if (button != null) {
               sudokuBoard.setByIndex(Integer.parseInt(clickedSudokuFieldButton.getId()),
                       Integer.parseInt(button.getText()));
               clickedSudokuFieldButton.setFont(new Font("Coryntia", 24));
           }
       } catch (NullPointerException npe) {
           throw new  NoButtonClicked(bundle
                   .getString("noSelectedFieldException"), npe);
       }
    }

    public void checkBoardClick() {
        if (sudokuBoard.checkBoard()) {
            resultLabel.setText(bundle.getString("correctLabel"));
            resultLabel.setTextFill(Color.rgb(0, 105, 0));
            resultLabel.setFont(Font.font(24));
        } else {
            resultLabel.setText(bundle.getString("incorrectLabel"));
            resultLabel.setTextFill(Color.rgb(169, 0, 0));
            resultLabel.setFont(Font.font(24));

            for (int i = 0; i < SudokuBoard.BOARD_SIZE; i++) {
                for (int j = 0; j < SudokuBoard.BOARD_SIZE; j++) {
                    if (sudokuBoard.get(j, i) != repository.sudokuBoard.get(j, i)) {
                        Button button = (Button) boardTable.getChildren().get((i + j * 9) + 1);
                        button.setBackground(new Background(new BackgroundFill(Color
                                .rgb(169, 0, 0), null, null)));
                    }
                }
            }
        }
    }

    public void loadGameFromDb(String saveName)  {
        Dao<Save> daoFactory = new DaoFactory().getJpaDao();
        List<Save> savesList = new ArrayList<>();
        try {
            savesList = daoFactory.read();
        } catch (MySqlException | NoFileException e) {
            log.error(bundle.getString(e.getLocalizedMessage()));
        }
        for (Save element : savesList) {
            if (Objects.equals(element.getSaveName(), saveName)) {
                this.sudokuBoard = element.getModifiedBoard();
                this.repository = new Repository(element.getOriginalBoard());
                this.sudokuBoard.updateFieldsProperty();
                this.repository.sudokuBoard.updateFieldsProperty();
                break;
            }
        }
    }

    public void loadGameFromFile(String name) {
        Dao<SudokuBoard> daoFactory = new DaoFactory().getFileDao(name);
        List<SudokuBoard> boards = new ArrayList<>();
        try {
            boards = daoFactory.read();
        } catch (NoFileException | MySqlException e) {
            log.error(bundle.getString(e.getLocalizedMessage()), e.getCause());
            alert.setContentText(bundle.getString("fileNotHaveSave"));
            alert.showAndWait();
        }

        this.repository = new Repository(boards.get(0));
        this.sudokuBoard = boards.get(1);
        this.sudokuBoard.updateFieldsProperty();
        this.repository.sudokuBoard.updateFieldsProperty();
    }

    public void saveGameAction() throws MySqlException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setGraphic(null);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(null);

        ButtonType saveToDbButton = new ButtonType(bundle.getString("saveGameToDb"));
        ButtonType saveToFileButton = new ButtonType((bundle.getString("saveGameToFile")));
        ButtonType cancelButton =
                new ButtonType((bundle.getString("cancelLabel")),
                        ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveToDbButton, saveToFileButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == saveToDbButton) {
            Dao<Save> daoFactory = new DaoFactory().getJpaDao();

            String saveName = "Save";
            int i = 1;

            do {
                saveName = saveName.substring(0, 4);
                saveName += String.valueOf(i);
                i++;
            }   while (daoFactory.readNames().contains(saveName));

            TextInputDialog dialog = new TextInputDialog(saveName);
            dialog.setGraphic(null);
            dialog.setHeaderText(null);
            dialog.setTitle(null);
            dialog.setContentText(bundle.getString("saveName"));
            Optional<String> resultName = dialog.showAndWait();

            if (resultName.get().length() < 50) {
                saveGameToDb(resultName.get());
            } else {
                alert.setContentText(bundle.getString("nameIsTooLong"));
                alert.showAndWait();
            }
        } else if (result.get() == saveToFileButton) {
            saveGameToFile();
        }
    }

    public void saveGameToDb(String saveName) {
        Dao<Save> daoFactory = new DaoFactory().getJpaDao();

        Save save = null;
        try {
            save = new Save(saveName, sudokuBoard.clone(), repository.sudokuBoard.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        try {
            if (!daoFactory.readNames().contains(saveName)) {
                daoFactory.write(save, save);
            } else {
                alert.setContentText(bundle.getString("saveNameAlreadyExist"));
                alert.showAndWait();
                saveGameAction();
            }
        } catch (MySqlException | CantSaveFileException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public void saveGameToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("saveGameButtonLabel"));
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(".save", "*.save"));
        File file = fileChooser.showSaveDialog(stage);

            try {
                Dao<SudokuBoard> daoFactory =
                        new DaoFactory().getFileDao(file.getAbsolutePath());
                daoFactory.write(repository.sudokuBoard, sudokuBoard);
            } catch (CantSaveFileException | MySqlException e) {
                log.error(bundle.getString(e.getLocalizedMessage()));
                alert.setContentText(bundle.getString("unableToSaveFile"));
            }
    }

    public void changeLevel() {
        menuController.getStage().setScene(menuController.getScene());
    }
}