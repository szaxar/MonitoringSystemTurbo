package monitoringsystemturbo;

import javafx.application.Application;
import javafx.stage.Stage;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.controller.ErrorController;
import monitoringsystemturbo.controller.MainController;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.ActivityMonitor;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.listeners.KeyboardListener;
import monitoringsystemturbo.model.listeners.MouseListener;
import monitoringsystemturbo.startup.Startup;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main extends Application {
    private static final String APPLICATION_NAME = "MonitoringSystemTurbo";

    private TrackingService trackingService;
    private MainExporter mainExporter;
    private ActivityMonitor activityMonitor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(APPLICATION_NAME);

        if (!Startup.isAppInStartup() && Startup.isAppJar()) {
            Startup.addAppToStartup();
        }

        registerHook();
        List<monitoringsystemturbo.model.app.Application> loadedApplications = null;
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            ErrorController.showError("Error occurred while reading from config file");
            System.exit(1);
        }
        mainExporter = new MainExporter();
        trackingService = new TrackingService(loadedApplications.stream()
                .map(it -> it.getName()).collect(Collectors.toList()));
        activityMonitor = new ActivityMonitor(trackingService);
        initializeEventListeners();
        trackingService.start();

        MainController mainController = new MainController(primaryStage, trackingService, mainExporter);
        mainController.showMainWindow(loadedApplications);

        primaryStage.setOnCloseRequest(event -> {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
            trackingService.stop();
            for (String appName : trackingService.getApplicationsNames()) {
                try {
                    StatisticsManager.save(appName, trackingService.getStatisticsForApp(appName));
                } catch (IOException e) {
                }
            }
            try {
                StatisticsManager.save(trackingService.getComputerStatistics());
            } catch (IOException e) {
            }
            try {
                activityMonitor.interrupt();
                activityMonitor.join();
            } catch (InterruptedException e) {
            }
        });
    }

    private void registerHook() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook. Extended monitoring will be disabled");
            System.err.println(ex.getMessage());
        }
    }

    private void initializeEventListeners() {
        MouseListener mouseListener = new MouseListener(activityMonitor);
        GlobalScreen.addNativeMouseListener(mouseListener);
        GlobalScreen.addNativeMouseMotionListener(mouseListener);
        GlobalScreen.addNativeMouseWheelListener(mouseListener);
        GlobalScreen.addNativeKeyListener(new KeyboardListener(activityMonitor));
        activityMonitor.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
