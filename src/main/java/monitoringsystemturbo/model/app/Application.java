package monitoringsystemturbo.model.app;

import sun.awt.shell.ShellFolder;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    public String getName() {
        return this.name;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public Icon loadIcon() {
        File file = new File(this.fullPath);
        ShellFolder sf = null;
        try {

            sf = ShellFolder.getShellFolder(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return (new ImageIcon(sf.getIcon(true)));
    }

    public void createFileIfNeeded() {
        File file = new File(this.name+".json");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}