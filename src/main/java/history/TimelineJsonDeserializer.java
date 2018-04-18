package history;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import timeline.ActivePeriod;
import timeline.Period;
import timeline.RunningPeriod;
import timeline.Timeline;

import java.io.IOException;
import java.util.Date;

public class TimelineJsonDeserializer extends Timeline {

    private JsonNode timelineJson;

    public TimelineJsonDeserializer(String timelineInJson) throws IOException, ClassNotFoundException {
        timelineJson = new ObjectMapper().readTree(timelineInJson);
        deserializeTimeline();
        deserializePeriods();
    }

    private void deserializeTimeline() {
        this.datetimeStart = new Date(timelineJson.get("datetimeStart").asLong());
        this.datetimeEnd = new Date(timelineJson.get("datetimeEnd").asLong());
    }

    private void deserializePeriods() throws ClassNotFoundException {
        JsonNode periodsJson = timelineJson.get("periods");
        for (JsonNode periodJson : periodsJson) {
            String periodClassName = periodJson.get("type").asText();
            Date datetimeStart = new Date(periodJson.get("datetimeStart").asLong());
            Date datetimeEnd = new Date(periodJson.get("datetimeEnd").asLong());
            Period period;
            switch (periodClassName) {
                case "ActivePeriod":
                    period = new ActivePeriod(datetimeStart, datetimeEnd);
                    break;
                case "RunningPeriod":
                    period = new RunningPeriod(datetimeStart, datetimeEnd);
                    break;
                default:
                    throw new ClassNotFoundException("Period subclass not found (" + periodClassName + ")");
            }
            periods.add(period);
        }
    }

    public Timeline getTimeline() {
        return this;
    }

}
