package monitoringsystemturbo.model.app;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;

public class Application {
	private String name;
	private String fullPath;
	private Icon icon;

	public Application() {
		this.name = "";
		this.fullPath = "";
	}

	public Application(String name, String fullPath) throws FileNotFoundException {
		this.name = name;
		this.fullPath = fullPath;
		this.icon=setIcon(fullPath);
	}

	public String getName() {
		return this.name;
	}

	public String getFullPath() {
		return this.fullPath;
	}

	private Icon setIcon(String fullPath) throws FileNotFoundException {
		Icon icon = FileSystemView.getFileSystemView()
				.getSystemIcon(new File(fullPath));
		return icon;
	}

	public Icon getIcon() {
		return icon;
	}
}
