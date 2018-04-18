package history;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import computer.ComputerStatistics;
import timeline.Timeline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StatisticsManager {

    private static final String computerStatisticsFilename = "computer-statistics.json";

    public static void save(ComputerStatistics computerStatistics) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("ComputerStatsJsonSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(ComputerStatistics.class, new ComputerStatsJsonSerializer());
        objectMapper.registerModule(module);

        PrintWriter writer = new PrintWriter(new FileWriter(computerStatisticsFilename, true));
        objectMapper.writeValue(writer, computerStatistics);
    }

    public static List<ComputerStatistics> loadComputerStats() throws IOException {
        ArrayList<ComputerStatistics> computerStatisticsList = new ArrayList<>();
        Stream<String> stream = Files.lines(Paths.get(computerStatisticsFilename));
        stream.forEach((String computerStatisticsJsonString) -> {
            if (computerStatisticsJsonString.length() == 0) {
                return;
            }
            try {
                JsonNode computerStatisticsJson = new ObjectMapper().readTree(computerStatisticsJsonString);
                ComputerStatsJsonDeserializer deserializer = new ComputerStatsJsonDeserializer(computerStatisticsJson);
                computerStatisticsList.add(deserializer.getComputerStatistics());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return computerStatisticsList;
    }

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
