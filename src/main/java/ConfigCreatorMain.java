import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigCreatorMain {
    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
        List<Program> listToSave = new ArrayList<Program>();
        listToSave.add(new Program("chrome", "C://eclipse/eclipse.exe"));
        listToSave.add(new Program("idea64", "C://programfiles/notepad/notepad.exe"));
        JsonManager.save(listToSave);
    }
}
