package monitoringsystemturbo.model.listeners;

import monitoringsystemturbo.model.ActionsMonitor;

import java.time.LocalTime;

abstract class EventListener {
    private ActionsMonitor actionsMonitor;

    EventListener(ActionsMonitor actionsMonitor) {
        this.actionsMonitor = actionsMonitor;
    }

    void notifyMonitors() {
        actionsMonitor.eventCaptured(LocalTime.now());
    }
}
