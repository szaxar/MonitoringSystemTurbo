package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import monitoringsystemturbo.controller.ConfirmExportController;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.LocalDate.now;

public class ExportPresenter {

    private Stage primaryStage;
    private MainExporter mainExporter;
    private TrackingService trackingService;

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXTimePicker toTimePicker;

    @FXML
    private JFXTimePicker fromTimePicker;

    @FXML
    private JFXCheckBox allTimeCheckBox;

    @FXML
    private JFXCheckBox fromBeginCheckBox;

    @FXML
    private JFXCheckBox toNowCheckBox;

    @FXML
    public void initialize() {
        fromDatePicker.setValue(now());
        toDatePicker.setValue(now());
        fromTimePicker.setValue(LocalTime.of(0, 0));
        toTimePicker.setValue(LocalTime.now());
    }

    @FXML
    public void onConfirm() {
        LocalDateTime fromTime = fromDatePicker.getValue().atTime(fromTimePicker.getValue().getHour(), fromTimePicker.getValue().getMinute());
        LocalDateTime toTime = toDatePicker.getValue().atTime(toTimePicker.getValue().getHour(), toTimePicker.getValue().getMinute());

        Date dateStart = Date.from(fromTime.atZone(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(toTime.atZone(ZoneId.systemDefault()).toInstant());

        if (dateStart.after(dateEnd)) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error occurred while checking date.");
            errorAlert.showAndWait();
        } else {
            ConfirmExportController confirmExportController = new ConfirmExportController(primaryStage);
            try {
                List<String> applicationsToExport = confirmExportController.showConfirmationAndGetAppList(trackingService.getApplicationsNames());
                if (!confirmExportController.getCancelValue()) {
                    if (allTimeCheckBox.isSelected()) {
                        mainExporter.export(trackingService, applicationsToExport);
                    } else {
                        mainExporter.export(trackingService, applicationsToExport, fromTime, toTime);
                    }
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success!");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data exported successfully! ");
                    successAlert.showAndWait();
                    primaryStage.close();
                }
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error occurred while exporting data.");
                errorAlert.showAndWait();
            }
        }
    }


    @FXML
    public void onToNow() {
        if (toNowCheckBox.isSelected()) {
            toDatePicker.setValue(now());
            toTimePicker.setValue(LocalTime.now());
            toDatePicker.setEditable(false);
            toTimePicker.setEditable(false);

            allTimeCheckBox.setDisable(true);
        } else {
            toDatePicker.setEditable(true);
            toTimePicker.setEditable(true);

            if (!fromBeginCheckBox.isSelected()) allTimeCheckBox.setDisable(false);
        }
    }

    @FXML
    public void onFromBegin() {
        if (fromBeginCheckBox.isSelected()) {
            fromDatePicker.setValue(LocalDate.of(1970, 1, 1));
            fromTimePicker.setValue(LocalTime.of(0, 0));
            fromDatePicker.setEditable(false);
            fromTimePicker.setEditable(false);

            allTimeCheckBox.setDisable(true);
        } else {
            fromDatePicker.setEditable(true);
            fromTimePicker.setEditable(true);

            if (!toNowCheckBox.isSelected()) allTimeCheckBox.setDisable(false);
        }
    }

    @FXML
    public void onAllTime() {
        if (allTimeCheckBox.isSelected()) {
            fromDatePicker.setValue(LocalDate.of(1970, 1, 1));
            fromTimePicker.setValue(LocalTime.of(0, 0));

            fromBeginCheckBox.setDisable(true);
            toNowCheckBox.setDisable(true);

            toDatePicker.setValue(now());
            toTimePicker.setValue(LocalTime.now());

            toDatePicker.setEditable(false);
            toTimePicker.setEditable(false);
            fromDatePicker.setEditable(false);
            fromTimePicker.setEditable(false);
        } else {
            toDatePicker.setEditable(true);
            toTimePicker.setEditable(true);
            fromDatePicker.setEditable(true);
            fromTimePicker.setEditable(true);

            fromBeginCheckBox.setDisable(false);
            toNowCheckBox.setDisable(false);
        }
    }


    @FXML
    public void onCancel() {
        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainExporter(MainExporter mainExporter) {
        this.mainExporter = mainExporter;
    }

    public void setTrackingService(TrackingService trackingService) {
        this.trackingService = trackingService;
    }
}
