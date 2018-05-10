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

    public void startMonitoring(String appName) {
        synchronized (appServicesToMonitor) {
            appServicesToMonitor.add(new ApplicationService(appName));
        }
    }

    public void stopMonitoring(String appName) throws IllegalStateException {
        synchronized (appServicesToMonitor) {
            ApplicationService service = appServicesToMonitor.stream()
                    .filter(applicationService -> applicationService.getApplicationName().equals(appName))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException("Try to stop monitoring app which was not added before"));
            appServicesToMonitor.remove(service);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    public List<ApplicationService> getAppServices() {
        synchronized (appServicesToMonitor) {
            return appServicesToMonitor;
        }
    }
}
