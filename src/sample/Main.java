package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.timeline.Timeline;

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
        Timeline timeline = appService.getTimeline();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Running time: " + timeline.getRunningTimeInSec());
        System.out.println("Active time: " + timeline.getActiveTimeInSec());

        launch(args);
    }
}
