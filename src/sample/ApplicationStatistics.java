package sample;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.*;

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
        String tasklistCommand = tasklistPath + " /FI \"IMAGENAME eq " + filename + "\"";
        try {
            Process process = Runtime.getRuntime().exec(tasklistCommand);
            BufferedReader input =  new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            do {
                line = input.readLine();
            } while (line != null && !line.startsWith(filename));
            input.close();
            return line != null && line.startsWith(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isActive() {
        byte[] windowText = new byte[512];
        PointerType hwnd = User32.INSTANCE.GetForegroundWindow(); // then you can call it!
        User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);
        System.out.println(Native.toString(windowText));

        return false;
    }

    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
        HWND GetForegroundWindow();  // add this
        int GetWindowTextA(PointerType hWnd, byte[] lpString, int nMaxCount);
    }

}
