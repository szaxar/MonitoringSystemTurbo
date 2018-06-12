package monitoringsystemturbo.model.app;

import sun.awt.shell.ShellFolder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Application {
    private final String userName;
    private final String fullPath;
    private String name;

    public Application() {
        this.userName = "";
        this.fullPath = "";
    }

    public Application(String userName, String fullPath) {
        this.userName = userName;
        this.fullPath = fullPath;
        resolveName(fullPath);
    }

    public Application(String userName) {
        this.userName = userName;
        this.fullPath = "";
        resolveName(fullPath);
    }

    private void resolveName(String fullPath) {
        String[] splitFullPath = fullPath.split("\\\\");
        this.name = splitFullPath[splitFullPath.length - 1]
                .split("\\.")[0];
    }

    public String getName() {
        return this.name;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public String getUserName() {
        return userName;
    }

    public Icon findIcon() {
        if (fullPath.equals("")) {
            return UIManager.getIcon("FileView.directoryIcon");
        }
        File file = new File(this.fullPath);
        ShellFolder sf = null;
        try {
            sf = ShellFolder.getShellFolder(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return (new ImageIcon(sf.getIcon(true)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(fullPath, that.fullPath) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, fullPath, name);
    }
}