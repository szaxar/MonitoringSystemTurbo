package history;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import timeline.Period;
import timeline.Timeline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StatisticsManager {

    public static void save(String applicationName, Timeline timeline) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("TimelineJsonSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(Timeline.class, new TimelineJsonSerializer());
        objectMapper.registerModule(module);

        String fileName = String.format("%s.json", applicationName);
        PrintWriter writer = new PrintWriter(new FileWriter(fileName, true));
        objectMapper.writeValue(writer, timeline);
    }

}
