package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.presenter.MainPresenter;
import java.io.IOException;
import java.util.List;

public class MainController {

    private Stage primaryStage;
    private TrackingService trackingService;
    private MainExporter mainExporter;

    public MainController(Stage primaryStage, TrackingService trackingService, MainExporter mainExporter) {
        this.primaryStage = primaryStage;
        this.trackingService = trackingService;
        this.mainExporter = mainExporter;
    }

    public void showMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/main.fxml"));
        Parent rootLayout = loader.load();

        openConfig();
        MainPresenter mainPresenter=loader.getController();
        mainPresenter.initialize(trackingService, mainExporter, loadedApplications);

        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();
    }



    private void openConfig(){
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error occurred while reading from config file");
        }
        if(loadedApplications == null){
            System.out.println("Error occurred while reading from config file");
            System.exit(1);
        }

    }
}
