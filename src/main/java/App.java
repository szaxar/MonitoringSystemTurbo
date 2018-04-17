import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		List<Program> listToSave = new ArrayList<Program>();
		listToSave.add(new Program("eclipse", "C://eclipse/eclipse.exe"));
		listToSave.add(new Program("notepad", "C://programfiles/notepad/notepad.exe"));
		JsonManager.save(listToSave);
		
		List<Program> loadedList = JsonManager.load();
		loadedList.add(new Program("saper", "D://saper.exe"));
		JsonManager.save(loadedList);
		
		System.out.println(loadedList.get(0).getFullPath());
		System.out.println(loadedList.get(1).getName());
	}
}
