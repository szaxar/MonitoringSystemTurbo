package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import monitoringsystemturbo.controller.ConfirmExportController;
import monitoringsystemturbo.controller.AlertController;
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
    private CheckBox wholeRangeCheckBox;

    @FXML
    private CheckBox fromBeggingCheckBox;

    @FXML
    private CheckBox untilNowCheckBox;

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
            AlertController.showAlert("Error occurred while checking date.", Alert.AlertType.ERROR);
        } else {
            ConfirmExportController confirmExportController = new ConfirmExportController(primaryStage);
            try {
                List<String> applicationsToExport = confirmExportController.showConfirmationAndGetAppList(trackingService.getApplicationsNames());
                if (!confirmExportController.getCancelValue()) {
                    if (wholeRangeCheckBox.isSelected()) {
                        mainExporter.export(trackingService, applicationsToExport);
                    } else {
                        mainExporter.export(trackingService, applicationsToExport, fromTime, toTime);
                    }
                    AlertController.showAlert("Data exported successfully!", Alert.AlertType.INFORMATION);
                    primaryStage.close();
                }
            } catch (IOException e) {
                AlertController.showAlert("Error occurred while exporting data.", Alert.AlertType.ERROR);
            }
        }
    }


    @FXML
    public void onToNow() {
        if (untilNowCheckBox.isSelected()) {
            toDatePicker.setValue(now().plusYears(1));
            toTimePicker.setValue(LocalTime.now());
            setDisableForToPickers(true);
            wholeRangeCheckBox.setDisable(true);
        } else {
            setDisableForToPickers(false);
            if (!fromBeggingCheckBox.isSelected()) wholeRangeCheckBox.setDisable(false);
        }
    }

    @FXML
    public void onFromBegin() {
        if (fromBeggingCheckBox.isSelected()) {
            fromDatePicker.setValue(LocalDate.of(1970, 1, 1));
            fromTimePicker.setValue(LocalTime.of(0, 0));
            setDisableToFromPickers(true);
            wholeRangeCheckBox.setDisable(true);
        } else {
            setDisableToFromPickers(false);
            if (!untilNowCheckBox.isSelected()) wholeRangeCheckBox.setDisable(false);
        }
    }

    @FXML
    public void onWholeRange() {
        if (wholeRangeCheckBox.isSelected()) {
            fromDatePicker.setValue(LocalDate.of(1970, 1, 1));
            fromTimePicker.setValue(LocalTime.of(0, 0));

            fromBeggingCheckBox.setDisable(true);
            untilNowCheckBox.setDisable(true);

            toDatePicker.setValue(now().plusYears(1));
            toTimePicker.setValue(LocalTime.now());

            setDisableToAllPickers(true);
        } else {
            setDisableToAllPickers(false);

            fromBeggingCheckBox.setDisable(false);
            untilNowCheckBox.setDisable(false);
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

    public void setDisableToAllPickers(boolean isDisable) {
        toDatePicker.setDisable(isDisable);
        toTimePicker.setDisable(isDisable);
        fromDatePicker.setDisable(isDisable);
        fromTimePicker.setDisable(isDisable);
    }

    public void setDisableForToPickers(boolean isDisable) {
        toDatePicker.setDisable(isDisable);
        toTimePicker.setDisable(isDisable);
    }

    public void setDisableToFromPickers(boolean isDisable) {
        fromDatePicker.setDisable(isDisable);
        fromTimePicker.setDisable(isDisable);
    }
}
