package json.json;

public class Program {
	private String name;
	private String fullPath;
	
	public Program() {
		this.name="";
		this.fullPath="";
	}
	
	public Program(String name, String fullPath) {
		this.name=name;
		this.fullPath=fullPath;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFullPath() {
		return this.fullPath;
	}

}
