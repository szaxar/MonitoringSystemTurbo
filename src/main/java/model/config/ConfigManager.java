package model.config;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
	private static ObjectMapper mapper = new ObjectMapper();

	private ConfigManager() {};

	public static void save(List<Program> list) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(new File("config.json"), list);
	}

	public static List<Program> load() throws JsonParseException, JsonMappingException, IOException {
		File file = new File("config.json");
		if(file.exists()) {
			List<Program> list = mapper.readValue(file, new TypeReference<List<Program>>() { });
			return list;
		}
		return new ArrayList<Program>();
	}
}
