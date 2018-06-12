package monitoringsystemturbo.model;

import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivityMonitor extends Thread {
    private static final int STOP_TIME = 5;

    private LocalTime lastUpdate = LocalTime.now();
    private AtomicBoolean isSleeping = new AtomicBoolean(false);
    private Lock timeLock = new ReentrantLock();
    private TrackingService trackingService;

    private boolean isRunning = true;

    public ActivityMonitor(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @Override
    public void run() {
        while (isRunning) {
            timeLock.lock();
            boolean shouldBeSlept = LocalTime.now().minus(STOP_TIME, ChronoUnit.MINUTES).compareTo(lastUpdate) > 0;
            timeLock.unlock();
            if (shouldBeSlept && !isSleeping.get()) {
                Map<String, Timeline> appStatistics = trackingService.getAllApplicationsStatistics();
                for (String appName : appStatistics.keySet()) {
                    try {
                        StatisticsManager.save(appName, appStatistics.get(appName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                trackingService.stop();
                isSleeping.set(true);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void interrupt() {
        isRunning = false;
        super.interrupt();
    }

    public void eventCaptured(LocalTime time) {
        timeLock.lock();
        this.lastUpdate = time;
        timeLock.unlock();
        if (isSleeping.get()) {
            trackingService.start();
            isSleeping.set(false);
        }
    }
}
