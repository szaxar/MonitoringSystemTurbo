package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class App {
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		List<Program> listToSave = new ArrayList<Program>();
		list.add(new Program("eclipse", "C://eclipse/eclipse.exe"));
		list.add(new Program("notepad", "C://programfiles/notepad/notepad.exe"));
		JsonManager.save(listToSave);
		
		List<Program> loadedList = JsonManager.load();
		loadedList.add(new Program("saper", "D://saper.exe"));
		JsonManager.save(loadedList);
		
		System.out.println(list2.get(0).getFullPath());
		System.out.println(list2.get(1).getName());
	}
}
