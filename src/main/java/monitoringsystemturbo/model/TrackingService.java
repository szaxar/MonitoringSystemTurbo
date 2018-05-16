package monitoringsystemturbo.model;

import monitoringsystemturbo.Main;
import monitoringsystemturbo.model.app.ApplicationMonitor;
import monitoringsystemturbo.model.app.ApplicationService;
import monitoringsystemturbo.model.computer.ComputerMonitor;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.MainPresenter;

import java.util.*;

public class TrackingService {
    private List<ApplicationService> applicationServices = new ArrayList<>();
    private ComputerStatistics computerStatistics;
    private ApplicationMonitor applicationMonitor;
    private ComputerMonitor computerMonitor;

    public TrackingService() {

    }

    public TrackingService(List<String> applicationNames) {
        for (String appName : applicationNames) {
            applicationServices.add(new ApplicationService(appName));
        }
    }

    public void addAppToMonitor(String appName) {
        applicationServices.add(new ApplicationService(appName));
    }

    public List<String> getApplicationsNames() {
        List<String> names = new ArrayList<>();
        for (ApplicationService applicationService : applicationServices) {
            names.add(applicationService.getApplicationName());
        }
        return names;
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
        throw new IllegalStateException("You did not specify such an app to monitor!");
    }

    public Map<String, Timeline> getAllApplicationsStatistics() {
        Map<String, Timeline> appMap = new HashMap<>();
        for (ApplicationService applicationService : applicationServices) {
            appMap.put(applicationService.getApplicationName(), applicationService.getTimeline());
        }
        return appMap;
    }
}
