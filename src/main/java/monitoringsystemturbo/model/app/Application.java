package monitoringsystemturbo.model.app;

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
}
