package sample;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import sample.timeline.Timeline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class ApplicationService {
    private static final String tasklistPath = System.getenv("windir") + "\\system32\\tasklist.exe";
    private final String filename;
    private final Timeline timeline = new Timeline();

    public ApplicationService(String filename) {
        this.filename = filename;
    }

    public ApplicationState getApplicationState() {
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
        return Integer.parseInt(processInfo
                .replaceAll(" {20}", " ")
                .split(" ")[1]);
    }

    public void updateTimeline() {
        Date currentDate = new Date();
        timeline.update(currentDate, getApplicationState());
    }

    public Timeline getTimeline() {
        return timeline;
    }
}