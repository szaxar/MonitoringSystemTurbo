package monitoringsystemturbo.startup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Startup {

    private final static String startupPath = "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
    private final static String shortcutName = "MonitoringSystemTurbo";
    private final static String startJarTask = "start javaw -jar";

    public static boolean isAppInStartup() {
        return getShortcutPath().toFile().exists();
    }

    public static boolean isAppJar() {
        return Paths.get(System.getProperty("java.class.path")).toFile().exists();
    }

    public static void addAppToStartup() {
        StandardOpenOption[] openOptions = new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE};
        byte[] shortcutContent = getShortcutContent().getBytes();
        try {
            Files.write(getShortcutPath(), shortcutContent, openOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path getShortcutPath() {
        return Paths.get(Startup.getStartupPath() + "\\" + Startup.shortcutName + ".bat");
    }

    public static String getStartupPath() {
        return System.getenv("appdata") + startupPath;
    }

    private static String getShortcutContent() {
        return startJarTask + " " + getAppJarPath();
    }

    private static String getAppJarPath() {
        return Paths.get(System.getProperty("java.class.path")).toFile().getAbsolutePath();
    }

}
