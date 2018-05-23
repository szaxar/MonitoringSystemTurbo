package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private JFXTimePicker toHoursPicker;

    @FXML
    private JFXTimePicker fromHoursPicker;


    @FXML
    private CheckBox allTimeCheckBox;

    @FXML
    public void initialize(){
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
    }


    public void onConfirm() {


        LocalDateTime fromTime=fromDatePicker.getValue().atTime(fromHoursPicker.getValue().getHour(),fromHoursPicker.getValue().getMinute());
        LocalDateTime toTime=toDatePicker.getValue().atTime(toHoursPicker.getValue().getHour(),toHoursPicker.getValue().getMinute());



        ExportWindowHandler exportWindowHandler = new ExportWindowHandler();
        List<String> applicationsToExport = exportWindowHandler.displayCheckingWindow(trackingService.getApplicationsNames());
        if (!exportWindowHandler.getCancelValue()) {
            try {

                if(allTimeCheckBox.isSelected()) {
                    mainExporter.export(trackingService, applicationsToExport);
                }
                else{
                    mainExporter.export(trackingService, applicationsToExport,fromTime,toTime);
                }
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Data exported successfully! ");
                successAlert.showAndWait();
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error occurred while exporting data.");
                errorAlert.showAndWait();
            }
        }

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onCancel() {
        primaryStage.close();
    }

    public void setMainExporter(MainExporter mainExporter) {
        this.mainExporter = mainExporter;
    }

    public void setTrackingService(TrackingService trackingService) {
        this.trackingService = trackingService;
    }
}
