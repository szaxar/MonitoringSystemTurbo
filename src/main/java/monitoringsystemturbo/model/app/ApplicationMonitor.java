package monitoringsystemturbo.model.app;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMonitor extends Thread {

    private boolean isRunning = true;
    private final List<ApplicationService> appServicesToMonitor;

    public ApplicationMonitor(List<ApplicationService> applicationServices) {
        appServicesToMonitor = applicationServices;
    }

    public ApplicationMonitor() {
        appServicesToMonitor = new ArrayList<>();
    }

    public void startMonitoring(ApplicationService appService) {
        synchronized (appServicesToMonitor) {
            appServicesToMonitor.add(appService);
        }
    }

    public void stopMonitoring(ApplicationService appService) {
        synchronized (appServicesToMonitor) {
            appServicesToMonitor.remove(appService);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            synchronized (appServicesToMonitor) {
                for (ApplicationService appService : appServicesToMonitor) {
                    appService.updateTimeline();
                }
            }
        }
    }

    @Override
    public void interrupt() {
        isRunning = false;
        super.interrupt();
    }
}
