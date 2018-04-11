package json.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class App
{
    public static void main( String[] args ) throws JsonGenerationException, JsonMappingException, IOException
    {
        System.out.println( "Hello JSON!" );
        List<Program> list = new ArrayList<Program>();
        list.add(new Program("eclipse", "C://eclipse/eclipse.exe"));
        list.add(new Program("notepad", "C://programfiles/notepad/notepad.exe"));
        
        JsonManager.save(list);
        
        List<Program> list2 = JsonManager.load();
        
        list2.add(new Program("aaa", "D://aa.exe"));
        
        JsonManager.save(list2);
        
        System.out.println(list2.get(0).getFullPath());
        System.out.println(list2.get(1).getName());
        
        
        
    }
}
