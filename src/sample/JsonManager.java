package sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import json.json.Program;

import com.fasterxml.jackson.core.type.TypeReference;

public class JsonManager {
	private static ObjectMapper mapper = new ObjectMapper();

	private JsonManager() {};

	public static void save(List<Program> list) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(new File("config.json"), list);
	}

	public static List<Program> load() throws JsonParseException, JsonMappingException, IOException {
		File file = new File("config.json");
		List<Program> list;
		if(file.exists()) {
			list = mapper.readValue(file, new TypeReference<List<Program>>() { });
			return list;
		}
		return new ArrayList<Program>();
	}
}
