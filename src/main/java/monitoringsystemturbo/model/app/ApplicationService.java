package monitoringsystemturbo.model.app;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import monitoringsystemturbo.model.OnTimeLineChangerListener;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.MainPresenter;
import monitoringsystemturbo.presenter.timeline.TimelineElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class ApplicationService {
    private static final String tasklistPath = System.getenv("windir") + "\\system32\\tasklist.exe";
    private final String filename;
    private final String appName;
    private final Timeline timeline = new Timeline();
    private OnTimeLineChangerListener listener;

    public ApplicationService(String appName, MainPresenter presenter) {
        this.listener = presenter;
        this.appName = appName;
        this.filename = String.format("%s.exe", appName);
    }

    public void updateTimeline() {
        Date currentDate = new Date();
        timeline.update(currentDate, getApplicationState());
        if(listener!=null)
            listener.onTimelineChange(timeline.getPeriods(), appName);
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public String getApplicationName() {
        return appName;
    }

    private ApplicationState getApplicationState() {
        try {
            String processInfo = getProcessInfo();
            if (processInfo == null) {
                return ApplicationState.NOT_RUNNING;
            }
            IntByReference pointer = new IntByReference();
            HWND hwnd = User32.INSTANCE.GetForegroundWindow();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pointer);
            if (getPid() == pointer.getValue()) {
                return ApplicationState.ACTIVE;
            }
            return ApplicationState.RUNNING;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApplicationState.NOT_RUNNING;
    }

    private String getProcessInfo() throws IOException {
        String tasklistCommand = tasklistPath + " /FI \"IMAGENAME eq " + filename + "\"";
        Process process = Runtime.getRuntime().exec(tasklistCommand);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        do {
            line = input.readLine();
        } while (line != null && !line.startsWith(filename));
        input.close();
        return line;
    }

    private int getPid() throws IOException {
        String processInfo = getProcessInfo();
        if (processInfo == null) {
            return -1;
        }
        boolean reachedFirstSpace = false;
        for (int i = 0; i < processInfo.length(); i++) {
            if (processInfo.charAt(i) == ' ') {
                reachedFirstSpace = true;
            } else if (processInfo.charAt(i) != ' ' && reachedFirstSpace) {
                processInfo = processInfo.substring(i);
                break;
            }
        }
        return Integer.parseInt(processInfo.split(" ")[0]);
    }
}
