package monitoringsystemturbo.model.app;

import sun.awt.shell.ShellFolder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class Application {
    private String name;
    private String fullPath;

    public Application() {
        this.name = "";
        this.fullPath = "";
    }

    public Application(String name, String fullPath) {
        this.name = name;
        this.fullPath = fullPath;
    }

    public Application(String name) {
        this.name = name;
        this.fullPath = "";
    }

    public String getName() {
        return this.name;
    }

    public String getFullPath() {
        return this.fullPath;
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
        return Objects.equals(name, that.name) &&
                Objects.equals(fullPath, that.fullPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fullPath);
    }
}