package monitoringsystemturbo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import monitoringsystemturbo.model.app.Application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
	private static ObjectMapper mapper = new ObjectMapper();

	private ConfigManager() {};

	public static void save(List<Application> list) throws IOException {
		mapper.writeValue(new File("config.json"), list);
	}

	public static List<Application> load() throws IOException {
		File file = new File("config.json");
		if(file.exists()) {
			List<Application> list = mapper.readValue(file, new TypeReference<List<Application>>() { });
			return list;
		}
		return new ArrayList<Application>();
	}

	public static void createFileIfNeeded(Application application) {
		File file = new File(application.getName()+".json");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
