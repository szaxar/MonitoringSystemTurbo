package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXColorPicker;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class MotivesPresenter {

    private Stage primaryStage;

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
    public void onConfirm(){
        textCollor = textColorPicker.getValue();
        controllerColor = controllersColorPicker.getValue();
        backgroundColor = backgroundColorPicker.getValue();
        ripplerColor = ripplerColorPicker.getValue();
        primaryStage.close();
    }


    @FXML
    public void initialize(){
        textColorPicker.setValue(textCollor);
        controllersColorPicker.setValue(controllerColor);
        backgroundColorPicker.setValue(backgroundColor);
        ripplerColorPicker.setValue(ripplerColor);
    }


    @FXML
    public void onCancel(){
        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
