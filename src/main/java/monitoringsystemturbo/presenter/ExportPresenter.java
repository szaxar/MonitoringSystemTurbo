package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ExportPresenter {


    private Stage primaryStage;
    private MainExporter mainExporter;
    private TrackingService trackingService;


    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private CheckBox allTimeCheckBox;

    @FXML
    public void initialize(){
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
    }


    public void onConfirm() {

        LocalDate fromDate=fromDatePicker.getValue();
        LocalDate toDate=toDatePicker.getValue();

        ExportWindowHandler exportWindowHandler = new ExportWindowHandler();
        List<String> applicationsToExport = exportWindowHandler.displayCheckingWindow(trackingService.getApplicationsNames());
        if (!exportWindowHandler.getCancelValue()) {
            try {

                if(allTimeCheckBox.isSelected()) {
                    mainExporter.export(trackingService, applicationsToExport);
                }
                else{
                    mainExporter.export(trackingService, applicationsToExport,fromDate,toDate);
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
