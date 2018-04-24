package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;

public class MainPresenter {
    private TrackingService trackingService;
    private MainExporter mainExporter;

    @FXML
    private ListView applicationList;

    @FXML
    public void initialize() {
        mainExporter = new MainExporter();

        trackingService = new TrackingService();
        trackingService.addAppToMonitor("idea64");  //just hardcoded for now, we'll change it
        trackingService.addAppToMonitor("chrome");
        trackingService.start();
    }

    @FXML
    public void onAddApplication() {
    }

    @FXML
    public void onRemoveApplication() {
        //pls remember to save statistics, otherwise data will be lost!
    }

    @FXML
    public void onExport() {
        ExportWindowHandler exportWindowHandler = new ExportWindowHandler();
        exportWindowHandler.displayCheckingWindow(trackingService.getApplicationsNames());
        try {
            mainExporter.export(trackingService);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success!");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Data exported successfully! ");
            successAlert.showAndWait();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error while exporting data.");
            errorAlert.showAndWait();
        }
    }

}
