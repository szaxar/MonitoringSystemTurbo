package monitoringsystemturbo.model;

import monitoringsystemturbo.model.app.ApplicationMonitor;
import monitoringsystemturbo.model.app.ApplicationService;
import monitoringsystemturbo.model.computer.ComputerMonitor;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.util.*;
import java.util.stream.Collectors;

public class TrackingService {
    private final List<String> applicationNames;

    private ComputerStatistics computerStatistics;
    private ApplicationMonitor applicationMonitor;
    private ComputerMonitor computerMonitor;

    public TrackingService() {
        applicationNames = new ArrayList<>();
    }

    public TrackingService(List<String> applicationNames) {
        this.applicationNames = applicationNames;
    }

    public void addAppToMonitor(String appName) {
        applicationNames.add(appName);
        applicationMonitor.startMonitoring(appName);
    }

    public void stopAppMonitoring(String appName) {
        applicationMonitor.stopMonitoring(appName);
        applicationNames.remove(appName);
    }

    public List<String> getApplicationsNames() {
        List<String> names = new ArrayList<>();
        for (ApplicationService applicationService : applicationMonitor.getAppServices()) {
            names.add(applicationService.getApplicationName());
        }
        return names;
    }

    public void start() {
        List<ApplicationService> appServices = applicationNames.stream()
                .map(ApplicationService::new)
                .collect(Collectors.toList());

        applicationMonitor = new ApplicationMonitor(appServices);
        computerStatistics = new ComputerStatistics(new Date());
        computerMonitor = new ComputerMonitor(computerStatistics);

        computerMonitor.start();
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
        for (ApplicationService applicationService : applicationMonitor.getAppServices()) {
            if (applicationService.getApplicationName().equals(applicationName)) {
                return applicationService.getTimeline();
            }
        }
        throw new IllegalStateException("You did not specify such an app to monitor!");
    }

    public Map<String, Timeline> getAllApplicationsStatistics() {
        Map<String, Timeline> appMap = new HashMap<>();
        for (ApplicationService applicationService : applicationMonitor.getAppServices()) {
            appMap.put(applicationService.getApplicationName(), applicationService.getTimeline());
        }
        return appMap;
    }
}
