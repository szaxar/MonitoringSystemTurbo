package history;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import timeline.Period;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StatisticsManager {
    public static void saveStatistics(String applicationName, List<Period> periods) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("CustomCarSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(Period.class, new PeriodJsonSerializer());
        objectMapper.registerModule(module);

        String fileName = String.format("%s.json", applicationName);
        PrintWriter writer = new PrintWriter(new FileWriter(fileName, true));
        objectMapper.writeValue(writer, periods);
    }
}
