package monitoringsystemturbo;

import javafx.stage.Stage;
import monitoringsystemturbo.exporter.Exporter;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.model.computer.ComputerMonitor;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Presentation extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MonitoringSystemTurbo");
    }

    public static void main(String[] args) throws FileNotFoundException {

        validateUserArguments(args);
        saveProcessesToConfig(Arrays.copyOfRange(args, 0, args.length - 1));

        System.out.println("Opening config file");
        List<Application> loadedApplications = null;
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error occurred while reading from config file");
        }
        if (loadedApplications == null) {
            System.out.println("Error occurred while reading from config file");
            System.exit(1);
        }
        List<String> programNames = new ArrayList<>();
        for (Application application : loadedApplications) {
            programNames.add(application.getName());
        }

        System.out.println("Starting monitoring...");
        TrackingService trackingService = new TrackingService(programNames);
        ComputerStatistics computerStatistics = new ComputerStatistics(new Date());
        ComputerMonitor computerMonitor = new ComputerMonitor(computerStatistics);
        computerMonitor.start();
        trackingService.start();
        try {
            Thread.sleep(Integer.parseInt(args[args.length - 1]) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        trackingService.stop();
        computerMonitor.interrupt();

        System.out.println("Computer statistics saved...");

        printStatistics(programNames, trackingService);
        saveStatistics(programNames, trackingService);
        exportData(programNames, trackingService);
        try {
            StatisticsManager.save(computerStatistics);
            List<ComputerStatistics> computerStatisticsList = StatisticsManager.loadComputerStats();
//            System.out.println(computerStatisticsList.size());
//            System.out.println(computerStatisticsList);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        launch(args);
    }

    private static void printStatistics(List<String> programNames, TrackingService trackingService) {
        System.out.println("ComputerStatistics: " + trackingService.getComputerStatistics().toString());
        for (String name : programNames) {
            Timeline statistics = trackingService.getStatisticsForApp(name);
            System.out.println(name + " background time: " + statistics.getRunningTimeInSec());
            System.out.println(name + " foreground time: " + statistics.getActiveTimeInSec());
        }
    }

    private static void saveStatistics(List<String> programNames, TrackingService trackingService) {
        for (String name : programNames) {
            Timeline statistics = trackingService.getStatisticsForApp(name);
            try {
                StatisticsManager.save(name, statistics);
                //List<Timeline> timelines = StatisticsManager.load("chrome");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All applications statistics saved...");
    }

    private static void exportData(List<String> programNames, TrackingService trackingService) {
        Exporter exporter = new Exporter("report");
        exporter.addComputerStatistics(trackingService.getComputerStatistics());
        for (String name : programNames) {
            Timeline statistics = trackingService.getStatisticsForApp(name);
            exporter.addApplicationStatistics(name, statistics);
        }
        try {
            exporter.exportGeneralInfo();
            exporter.exportDetailInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void saveProcessesToConfig(String[] processNames) throws FileNotFoundException {
        List<Application> listToSave = new ArrayList<Application>();
        for (String processName : processNames) {
            listToSave.add(new Application(processName, "C://Users/kkal6/AppData/Local/Google/Chrome SxS/Application/chrome.exe"));
        }
        try {
            ConfigManager.save(listToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved process names to config file...");
    }

    private static void validateUserArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: [name] [process1] [process2] ... [processN] [TimeToMonitor]");
            System.exit(1);
        } else {
            try {
                int parsedInt = Integer.parseInt(args[args.length - 1]);
            } catch (NumberFormatException e) {
                System.out.println("Usage: [name] [process1] [process2] ... [processN] [TimeToMonitor]");
                System.exit(1);
            }
        }
    }
}
