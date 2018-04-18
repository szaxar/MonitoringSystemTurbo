package model.history;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.timeline.Period;
import model.timeline.Timeline;

import java.io.IOException;

public class TimelineJsonSerializer extends StdSerializer<Timeline> {

    public TimelineJsonSerializer() {
        this(null);
    }

    public TimelineJsonSerializer(Class<Timeline> t) {
        super(t);
    }

    @Override
    public void serialize(Timeline timeline, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("datetimeStart", timeline.getDatetimeStart().getTime());
        jsonGenerator.writeNumberField("datetimeEnd", timeline.getDatetimeEnd().getTime());
        jsonGenerator.writeArrayFieldStart("periods");
        for (Period period : timeline.getPeriods()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", period.getClass().getSimpleName());
            jsonGenerator.writeNumberField("datetimeStart", period.getDatetimeStart().getTime());
            jsonGenerator.writeNumberField("datetimeEnd", period.getDatetimeEnd().getTime());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.writeRaw('\n');
    }
}
