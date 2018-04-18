package model;

import model.app.ApplicationMonitor;
import model.app.ApplicationService;
import model.computer.ComputerMonitor;
import model.computer.ComputerStatistics;
import model.timeline.Timeline;

import java.util.*;

public class TrackingService {
    private List<ApplicationService> applicationServices = new ArrayList<>();
    private ComputerStatistics computerStatistics;
    private ApplicationMonitor applicationMonitor;
    private ComputerMonitor computerMonitor;

    public TrackingService(List<String> applicationNames) {
        for (String appName : applicationNames) {
            applicationServices.add(new ApplicationService(appName));
        }
    }

    public void start() {
        computerStatistics = new ComputerStatistics(new Date());
        computerMonitor = new ComputerMonitor(computerStatistics);
        computerMonitor.start();
        applicationMonitor = new ApplicationMonitor(applicationServices);
        applicationMonitor.start();
    }

    public void stop() {
        try {
            computerMonitor.interrupt();
            computerMonitor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            applicationMonitor.interrupt();
            applicationMonitor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ComputerStatistics getComputerStatistics() {
        return computerStatistics;
    }

    public Timeline getStatisticsForApp(String applicationName) {
        for (ApplicationService applicationService : applicationServices) {
            if (applicationService.getApplicationName().equals(applicationName)) {
                return applicationService.getTimeline();
            }
        }
        throw new IllegalStateException("You did not specify such an model.app to monitor!");
    }

    public Map<String, Timeline> getAllApplicationsStatistics() {
        Map<String, Timeline> appMap = new HashMap<>();
        for (ApplicationService applicationService : applicationServices) {
            appMap.put(applicationService.getApplicationName(), applicationService.getTimeline());
        }
        return appMap;
    }
}
