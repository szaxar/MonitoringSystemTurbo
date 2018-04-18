package history;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import timeline.Timeline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StatisticsManager {

    public static void save(String applicationName, Timeline timeline) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("TimelineJsonSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(Timeline.class, new TimelineJsonSerializer());
        objectMapper.registerModule(module);

        String filename = getFilename(applicationName);
        PrintWriter writer = new PrintWriter(new FileWriter(filename, true));
        objectMapper.writeValue(writer, timeline);
    }

    public static List<Timeline> load(String applicationName) throws IOException {
        String filename = getFilename(applicationName);
        ArrayList<Timeline> timelines = new ArrayList<>();
        Stream<String> stream = Files.lines(Paths.get(filename));
        stream.forEach((String timelineInJson) -> {
            if (timelineInJson.length() == 0) {
                return;
            }
            try {
                TimelineJsonDeserializer deserializer = new TimelineJsonDeserializer(timelineInJson);
                timelines.add(deserializer.getTimeline());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return timelines;
    }

    private static String getFilename(String applicationName) {
        return String.format("%s.json", applicationName);
    }

}
