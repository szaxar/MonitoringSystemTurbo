package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXColorPicker;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import monitoringsystemturbo.controller.MainController;


public class MotivesPresenter {

    private Stage primaryStage;
    private MainController mainController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    JFXColorPicker backgroundColorPicker;

    @FXML
    JFXColorPicker controllersColorPicker;

    @FXML
    JFXColorPicker ripplerColorPicker;

    @FXML
    JFXColorPicker textColorPicker;

    public static Color textCollor = Color.WHITE;
    public static Color controllerColor = Color.BLACK;
    public static Color backgroundColor = Color.WHITE;
    public static Color ripplerColor = Color.RED;

    @FXML
    public void onConfirm() {
        textCollor = textColorPicker.getValue();
        controllerColor = controllersColorPicker.getValue();
        backgroundColor = backgroundColorPicker.getValue();
        ripplerColor = ripplerColorPicker.getValue();
        mainController.reflesh();
        primaryStage.close();

    }

    @FXML
    public void initialize() {
        textColorPicker.setValue(textCollor);
        controllersColorPicker.setValue(controllerColor);
        backgroundColorPicker.setValue(backgroundColor);
        ripplerColorPicker.setValue(ripplerColor);
    }
    
    public void reflesh() {
        anchorPane.setStyle("text-collor: #" + textCollor.toString().substring(2, 8) + ";" +
                "controller-color: #" + controllerColor.toString().substring(2, 8) + ";" +
                "background-collor: #" + backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-collor: #" + ripplerColor.toString().substring(2, 8) + ";");
    }

    @FXML
    public void onCancel() {
        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
