package monitoringsystemturbo.model.listeners;

import monitoringsystemturbo.model.ActivityMonitor;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class MouseListener extends EventListener implements NativeMouseInputListener, NativeMouseWheelListener {
    public MouseListener(ActivityMonitor activityMonitor) {
        super(activityMonitor);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
        notifyMonitors();
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        notifyMonitors();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        notifyMonitors();
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        notifyMonitors();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        notifyMonitors();
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
        notifyMonitors();
    }
}
