package sample;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApplicationStatistics {
    private static final String tasklistPath = System.getenv("windir") + "\\system32\\tasklist.exe";
    private final String filename;

    public ApplicationStatistics(String filename) {
        this.filename = filename;
    }

    public boolean isRunning() {
        try {
            return getProcessInfo().startsWith(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isActive() {
        IntByReference pointer = new IntByReference();
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, pointer);

        try {
            return getPid() == pointer.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getPid() throws IOException {
        return Integer.parseInt(getProcessInfo()
                .replaceAll(" {20}", " ")
                .split(" ")[1]);
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
        if (line != null && line.startsWith(filename)) {
            return line;
        } else {
            return "";
        }
    }
}
