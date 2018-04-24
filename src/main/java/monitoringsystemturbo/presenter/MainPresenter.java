package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

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
        mainExporter.export(trackingService);
    }

}
