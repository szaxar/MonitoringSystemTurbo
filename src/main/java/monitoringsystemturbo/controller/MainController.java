package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.ActionsMonitor;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.presenter.MainPresenter;

import java.io.IOException;
import java.util.List;

public class MainController {

    private Stage primaryStage;
    private TrackingService trackingService;
    private MainExporter mainExporter;
    private MainPresenter mainPresenter;
    private ActionsMonitor actionsMonitor;

    public MainController(Stage primaryStage, TrackingService trackingService, MainExporter mainExporter, ActionsMonitor actionsMonitor) {
        this.primaryStage = primaryStage;
        this.trackingService = trackingService;
        this.mainExporter = mainExporter;
        this.actionsMonitor = actionsMonitor;
    }

    public void showMainWindow(List<Application> loadedApplications) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/main.fxml"));
        Parent rootLayout = loader.load();

        MainPresenter mainPresenter = loader.getController();
        mainPresenter.initialize(trackingService, mainExporter, loadedApplications, actionsMonitor);

        ApplicationListController applicationListController = new ApplicationListController(primaryStage);
        mainPresenter.setApplicationListController(applicationListController);

        ExportController exportController = new ExportController(primaryStage);
        mainPresenter.setExportController(exportController);

        OptionsController optionsController = new OptionsController(primaryStage);
        mainPresenter.setOptionsController(optionsController);

        MotivesController motivesController = new MotivesController(primaryStage);
        mainPresenter.setMotivesController(motivesController);
        mainPresenter.setMainControler(this);

        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();
    }

    public void reflesh() {
        mainPresenter.reflesh();
    }
}
