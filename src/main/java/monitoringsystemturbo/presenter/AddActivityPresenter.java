package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import monitoringsystemturbo.model.app.Application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

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
    private Button addButton;

    @FXML
    private AnchorPane anchorPane;

    private boolean isNameEmpty = true;

    private Application application;
    private Date fromDate;
    private Date toDate;

    @FXML
    public void initialize() {
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
        fromTimePicker.setValue(LocalTime.of(0, 0));
        toTimePicker.setValue(LocalTime.of(23, 59));
        reflesh();
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
        LocalDateTime fromTime = fromDatePicker.getValue().atTime(fromTimePicker.getValue().getHour(), fromTimePicker.getValue().getMinute());
        LocalDateTime toTime = toDatePicker.getValue().atTime(toTimePicker.getValue().getHour(), toTimePicker.getValue().getMinute());
        fromDate = Date.from(fromTime.atZone(ZoneId.systemDefault()).toInstant());
        toDate = Date.from(toTime.atZone(ZoneId.systemDefault()).toInstant());
        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void onCancel() {
        primaryStage.close();
    }

    public Application getActivity() {
        return application;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void reflesh() {
        anchorPane.setStyle("text-color: #" + MotivesPresenter.textColor.toString().substring(2, 8) + ";" +
                "controller-color: #" + MotivesPresenter.controllerColor.toString().substring(2, 8) + ";" +
                "background-color: #" + MotivesPresenter.backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-color: #" + MotivesPresenter.ripplerColor.toString().substring(2, 8) + ";"+
                "second-color: #" + MotivesPresenter.secondColor.toString().substring(2, 8) + ";"+
                "background-text-color: #" + MotivesPresenter.backgroundTextColor.toString().substring(2, 8) + ";");
    }
}
