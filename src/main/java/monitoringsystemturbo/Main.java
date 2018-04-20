package monitoringsystemturbo;

import javafx.application.Application;
import monitoringsystemturbo.controller.MainController;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MonitoringSystemTurbo");
        MainController mainController = new MainController(primaryStage);
        mainController.showMainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
