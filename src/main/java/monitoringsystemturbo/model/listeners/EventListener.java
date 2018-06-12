package monitoringsystemturbo.model.listeners;

import monitoringsystemturbo.model.ActivityMonitor;

import java.time.LocalTime;

abstract class EventListener {
    private ActivityMonitor activityMonitor;

    EventListener(ActivityMonitor activityMonitor) {
        this.activityMonitor = activityMonitor;
    }

    void notifyMonitors() {
        activityMonitor.eventCaptured(LocalTime.now());
    }
}
