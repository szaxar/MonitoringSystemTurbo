package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXColorPicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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

    @FXML
    JFXColorPicker secondColorPicker;

    @FXML
    ChoiceBox choiceBox;

    public static Color textColor = Color.WHITE;
    public static Color controllerColor = Color.BLACK;
    public static Color backgroundColor = Color.WHITE;
    public static Color ripplerColor = Color.RED;
    public static Color secondColor = Color.GREY;

    @FXML
    public void onConfirm() {
        textColor = textColorPicker.getValue();
        controllerColor = controllersColorPicker.getValue();
        backgroundColor = backgroundColorPicker.getValue();
        ripplerColor = ripplerColorPicker.getValue();
        secondColor = secondColorPicker.getValue();
        mainController.reflesh();
        primaryStage.close();
    }

    @FXML
    public void initialize() {
        textColorPicker.setValue(textColor);
        controllersColorPicker.setValue(controllerColor);
        backgroundColorPicker.setValue(backgroundColor);
        ripplerColorPicker.setValue(ripplerColor);
        secondColorPicker.setValue(secondColor);

        choiceBox.setItems(FXCollections.observableArrayList(
                "Standard", "Dracula"));

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                changeMotive(newValue);
            }
        });
    }

    private void changeMotive(Number newValue) {
        switch (newValue.intValue()){
            case 0:
                setStandardMode();
                break;
            case 1:
                setDraculaMode();
                break;
        }
    }

    public void reflesh() {
        anchorPane.setStyle("text-color: #" + textColor.toString().substring(2, 8) + ";" +
                "controller-color: #" + controllerColor.toString().substring(2, 8) + ";" +
                "background-color: #" + backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-color: #" + ripplerColor.toString().substring(2, 8) + ";"+
                "secound-color: #" + secondColor.toString().substring(2, 8) + ";");
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


    public void setStandardMode(){
        controllersColorPicker.setValue(Color.BLACK);
        backgroundColorPicker.setValue(Color.WHITE);
        ripplerColorPicker.setValue(Color.RED);
        secondColorPicker.setValue(Color.GREY);
        textColorPicker.setValue(Color.WHITE);
    }

    public void setDraculaMode(){
        controllersColorPicker.setValue(Color.WHITE);
        backgroundColorPicker.setValue(Color.BLACK);
        ripplerColorPicker.setValue(Color.RED);
        secondColorPicker.setValue(Color.GREY);
        textColorPicker.setValue(Color.BLACK);
    }
}
