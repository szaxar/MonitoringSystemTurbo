package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import monitoringsystemturbo.controller.ConfirmExportController;
import monitoringsystemturbo.controller.ErrorController;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
    private CheckBox allTimeCheckBox;

    @FXML
    public void initialize() {
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
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
            ErrorController.showError("Error occurred while checking date.", Alert.AlertType.ERROR);
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
                ErrorController.showError("Error occurred while exporting data.", Alert.AlertType.ERROR);
            }
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
