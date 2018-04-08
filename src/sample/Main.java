package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

        Calendar systemStartTime = Calendar.getInstance();
        ComputerStatistics computerStatistics=new ComputerStatistics(systemStartTime);


        launch(args);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Calendar systemCloseTime = Calendar.getInstance();
                computerStatistics.setSystemCloseTime(systemCloseTime);
                System.out.println(computerStatistics.toString());
            }
        }, "Shutdown-thread"));



    }
}
