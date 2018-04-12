package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Date systemStartTime = new Date();
        ComputerStatistics computerStatistics = new ComputerStatistics(systemStartTime);
        Runtime.getRuntime().addShutdownHook(new ShutDownThread(computerStatistics));


        ApplicationMonitor appMonitor = new ApplicationMonitor();
        appMonitor.start();
        ApplicationService appService = new ApplicationService("idea64.exe");
        appMonitor.startMonitoring(appService);

        System.out.println(appService.getTimeline());

        launch(args);
    }
}
