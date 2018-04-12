package sample;

import java.util.ArrayList;

public class ApplicationMonitor extends Thread {

    private boolean isRunning = true;
    private final ArrayList<ApplicationService> appServicesToMonitor = new ArrayList<>();

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
        super.interrupt();
        isRunning = false;
    }
}
