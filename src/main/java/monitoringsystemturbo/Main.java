package monitoringsystemturbo;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.controller.AlertController;
import monitoringsystemturbo.controller.MainController;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.ActionsMonitor;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.listeners.KeyboardListener;
import monitoringsystemturbo.model.listeners.MouseListener;
import monitoringsystemturbo.startup.Startup;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static final String APPLICATION_NAME = "MonitoringSystemTurbo";

    private TrackingService trackingService;
    private MainExporter mainExporter;
    private ActionsMonitor actionsMonitor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(APPLICATION_NAME);

        File historyDir = new File(StatisticsManager.historyDirName);
        if (!historyDir.exists()) {
            historyDir.mkdir();
        }

        if (!Startup.isAppInStartup() && Startup.isAppJar()) {
            Startup.addAppToStartup();
        }

        registerHook();
        List<monitoringsystemturbo.model.app.Application> loadedApplications = null;
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            AlertController.showAlert("Error occurred while reading from config file", Alert.AlertType.ERROR);
            System.exit(1);
        }
        mainExporter = new MainExporter();
        List<String> names = new ArrayList<>();
        for (monitoringsystemturbo.model.app.Application application : loadedApplications) {
            if (!application.getFullPath().equals(""))
                names.add(application.getName());
        }
        trackingService = new TrackingService(names);
        actionsMonitor = new ActionsMonitor(trackingService);
        initializeEventListeners();
        trackingService.start();

        MainController mainController = new MainController(primaryStage, trackingService, mainExporter, actionsMonitor);
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
                    e.printStackTrace();
                }
            }
            try {
                StatisticsManager.save(trackingService.getComputerStatistics());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                actionsMonitor.interrupt();
                actionsMonitor.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        MouseListener mouseListener = new MouseListener(actionsMonitor);
        GlobalScreen.addNativeMouseListener(mouseListener);
        GlobalScreen.addNativeMouseMotionListener(mouseListener);
        GlobalScreen.addNativeMouseWheelListener(mouseListener);
        GlobalScreen.addNativeKeyListener(new KeyboardListener(actionsMonitor));
        actionsMonitor.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
