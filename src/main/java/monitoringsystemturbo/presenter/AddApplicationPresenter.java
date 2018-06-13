package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import monitoringsystemturbo.controller.AlertController;
import monitoringsystemturbo.model.app.Application;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class AddApplicationPresenter {

    @FXML
    private Button addButton;
    @FXML
    private TextField nameApplication;
    @FXML
    private TextField fullPathApplication;
    @FXML
    private BorderPane borderPane;

    private Stage primaryStage;
    private Application application;
    private boolean isNameEmpty = true;
    private boolean isPathEmpty = true;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void initialize() {
        nameApplication.textProperty().addListener((observable, oldValue, newValue) -> {
            isNameEmpty = newValue.isEmpty();
            resolveButtonStatus();
        });
        fullPathApplication.textProperty().addListener((observable, oldValue, newValue) -> {
            isPathEmpty = newValue.isEmpty();
            resolveButtonStatus();
        });
        reflesh();
    }

    @FXML
    public void onAdd() {
        if (isPathCorrect()) {
            application = new Application(nameApplication.getText(), fullPathApplication.getText());
            primaryStage.close();
        } else {
            AlertController.showAlert("Incorrect path!", Alert.AlertType.ERROR);
        }
    }

    private boolean isPathCorrect() {
        String path = fullPathApplication.getText();
        File file = new File(path);
        return file.exists() && path.endsWith(".exe");
    }

    @FXML
    public void onFind() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select EXE file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("EXE Files", "*.exe"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if (nameApplication.getText().isEmpty()) {
                nameApplication.setText(FilenameUtils.getBaseName(selectedFile.getName()));
            }
            fullPathApplication.setText(selectedFile.getAbsolutePath());
            addButton.setDisable(false);
        }
    }

    @FXML
    public void onCancel() {
        primaryStage.close();
    }

    public Application getApplication() {
        return application;
    }

    private void resolveButtonStatus() {
        addButton.setDisable(isNameEmpty || isPathEmpty);
    }

    public void reflesh() {
        borderPane.setStyle("text-color: #" + MotivesPresenter.textColor.toString().substring(2, 8) + ";" +
                "controller-color: #" + MotivesPresenter.controllerColor.toString().substring(2, 8) + ";" +
                "background-color: #" + MotivesPresenter.backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-color: #" + MotivesPresenter.ripplerColor.toString().substring(2, 8) + ";"+
                "second-color: #" + MotivesPresenter.secondColor.toString().substring(2, 8) + ";"+
                "background-text-color: #" + MotivesPresenter.backgroundTextColor.toString().substring(2, 8) + ";");
    }
}
