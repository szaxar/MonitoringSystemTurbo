package monitoringsystemturbo;

import javafx.application.Application;
import javafx.scene.control.Alert;
import monitoringsystemturbo.startup.Startup;
import javafx.stage.Stage;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.controller.ErrorController;
import monitoringsystemturbo.controller.MainController;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.TrackingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static final String APPLICATION_NAME = "MonitoringSystemTurbo";

    private TrackingService trackingService;
    private MainExporter mainExporter;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(APPLICATION_NAME);

        if (!Startup.isAppInStartup() && Startup.isAppJar()) {
            Startup.addAppToStartup();
        }

        mainExporter = new MainExporter();
        trackingService = new TrackingService();
        List<monitoringsystemturbo.model.app.Application> loadedApplications = null;
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            ErrorController.showError("Error occurred while reading from config file", Alert.AlertType.ERROR);
            System.exit(1);
        }
        initializeAppsToMonitor(loadedApplications);
        trackingService.start();

        MainController mainController = new MainController(primaryStage, trackingService, mainExporter);
        mainController.showMainWindow(loadedApplications);

        primaryStage.setOnCloseRequest(event -> {
            trackingService.stop();
            for (String appName : trackingService.getApplicationsNames()) {
                try {
                    StatisticsManager.save(appName, trackingService.getStatisticsForApp(appName));
                } catch (IOException e) {}
            }
            try {
                StatisticsManager.save(trackingService.getComputerStatistics());
            } catch (IOException e) {}
        });
    }

    private void initializeAppsToMonitor(List<monitoringsystemturbo.model.app.Application> loadedApplications) {
        for (monitoringsystemturbo.model.app.Application application : loadedApplications) {
            trackingService.addAppToMonitor(application.getName());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
