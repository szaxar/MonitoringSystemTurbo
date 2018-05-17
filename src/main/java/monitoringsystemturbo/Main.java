package monitoringsystemturbo;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.controller.ErrorController;
import monitoringsystemturbo.controller.MainController;
import javafx.stage.Stage;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private TrackingService trackingService;
    private MainExporter mainExporter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MonitoringSystemTurbo");

        mainExporter = new MainExporter();
        trackingService = new TrackingService();
        List<monitoringsystemturbo.model.app.Application> loadedApplications = null;
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            ErrorController.showError("Error occurred while reading from config file");
            System.exit(1);
        }
        initializeAppsToMonitor(loadedApplications);
        trackingService.start();

        MainController mainController = new MainController(primaryStage, trackingService, mainExporter);
        mainController.showMainWindow(loadedApplications);

        primaryStage.setOnCloseRequest(event -> trackingService.stop());
    }

    private void initializeAppsToMonitor(List<monitoringsystemturbo.model.app.Application> loadedApplications) {
        for(monitoringsystemturbo.model.app.Application application : loadedApplications){
            trackingService.addAppToMonitor(application.getName());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
