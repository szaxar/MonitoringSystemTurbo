package monitoringsystemturbo.model.listeners;

import monitoringsystemturbo.model.ActionsMonitor;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyboardListener extends EventListener implements NativeKeyListener {
    public KeyboardListener(ActionsMonitor actionsMonitor) {
        super(actionsMonitor);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        notifyMonitors();
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
