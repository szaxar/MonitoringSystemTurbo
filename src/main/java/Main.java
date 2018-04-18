import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TrackingService;
import model.computer.ComputerMonitor;
import model.computer.ComputerStatistics;
import model.history.StatisticsManager;
import model.timeline.Timeline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {

        validateUserArguments(args);
        saveProcessesToConfig(Arrays.copyOfRange(args, 0, args.length-1));

        System.out.println("Opening config file");
        List<Program> loadedPrograms = null;
        try {
            loadedPrograms = JsonManager.load();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error occurred while reading from config file");
        }
        if(loadedPrograms == null){
            System.out.println("Error occurred while reading from config file");
            System.exit(1);
        }
        List<String> programNames = new ArrayList<>();
        for(Program program : loadedPrograms){
            programNames.add(program.getName());
        }

        System.out.println("Starting monitoring...");
        TrackingService trackingService = new TrackingService(programNames);
        ComputerStatistics computerStatistics = new ComputerStatistics(new Date());
        ComputerMonitor computerMonitor = new ComputerMonitor(computerStatistics);
        computerMonitor.start();
        trackingService.start();
        try {
            Thread.sleep(Integer.parseInt(args[args.length-1])*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        trackingService.stop();
        computerMonitor.interrupt();



        System.out.println("Computer statistics saved...");

        printStatistics(programNames, trackingService);
        try {
            StatisticsManager.save(computerStatistics);
            List<ComputerStatistics> computerStatisticsList = StatisticsManager.loadComputerStats();
//            System.out.println(computerStatisticsList.size());
//            System.out.println(computerStatisticsList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    private static void printStatistics(List<String> programNames, TrackingService trackingService) {

        for(String name : programNames){
            Timeline statistics = trackingService.getStatisticsForApp(name);
            System.out.println("ComputerStatistics: " + trackingService.getComputerStatistics().toString());
            System.out.println(name+ " background time: " + statistics.getRunningTimeInSec());
            System.out.println(name +" foreground time: " + statistics.getActiveTimeInSec());

            try {
                StatisticsManager.save(name, statistics);
                //List<Timeline> timelines = StatisticsManager.load("chrome");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.out.println("All applications statistics saved...");

    }

    private static void saveProcessesToConfig(String[] processNames) {
        List<Program> listToSave = new ArrayList<Program>();
        for(String processName : processNames){
            listToSave.add(new Program(processName, "C://"));
        }
        try {
            JsonManager.save(listToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved process names to config file...");
    }

    private static void validateUserArguments(String[] args) {
        if(args.length==0){
            System.out.println("Usage: [name] [process1] [process2] ... [processN] [TimeToMonitor]");
            System.exit(1);
        } else{
            try {
                int parsedInt = Integer.parseInt(args[args.length-1]);
            } catch(NumberFormatException e) {
                System.out.println("Usage: [name] [process1] [process2] ... [processN] [TimeToMonitor]");
                System.exit(1);
            }
        }
    }
}
