package history;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import timeline.Period;

import java.io.IOException;

public class PeriodJsonSerializer extends StdSerializer<Period> {

    public PeriodJsonSerializer() {
        this(null);
    }

    public PeriodJsonSerializer(Class<Period> t) {
        super(t);
    }

    @Override
    public void serialize(Period period, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("datetimeStart", period.getDatetimeStart().toString());
        jsonGenerator.writeStringField("datetimeEnd", period.getDatetimeEnd().toString());
        jsonGenerator.writeStringField("type", period.getClass().getSimpleName());
        jsonGenerator.writeEndObject();
    }
}
