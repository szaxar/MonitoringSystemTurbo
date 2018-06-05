package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import monitoringsystemturbo.model.app.Application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AddActivityPresenter {

    private Stage primaryStage;

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXTimePicker toTimePicker;

    @FXML
    private JFXTimePicker fromTimePicker;

    @FXML
    private TextField activityName;

    @FXML
    Button addButton;

    private boolean isNameEmpty = true;

    private Application application;

    @FXML
    public void initialize() {
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
        fromTimePicker.setValue(LocalTime.of(0, 0));
        toTimePicker.setValue(LocalTime.of(23, 59));

        setActivityNameTextListener();
    }

    private void setActivityNameTextListener() {
        activityName.textProperty().addListener((observable, oldValue, newValue) -> {
            isNameEmpty = newValue.isEmpty();
            addButton.setDisable(isNameEmpty);
        });
    }

    @FXML
    public void onAdd() {
        application = new Application(activityName.getText());
        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onCancel() {
        primaryStage.close();
    }

    public Application getActivity() {
        return application;
    }
}
