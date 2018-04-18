package model.history;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.computer.ComputerStatistics;

import java.io.IOException;

public class ComputerStatsJsonSerializer extends StdSerializer<ComputerStatistics> {

    public ComputerStatsJsonSerializer() {
        this(null);
    }

    public ComputerStatsJsonSerializer(Class<ComputerStatistics> t) {
        super(t);
    }

    @Override
    public void serialize(ComputerStatistics computerStatistics, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("systemStartTime", computerStatistics.getSystemStartTime().getTime());
        jsonGenerator.writeNumberField("systemCloseTime", computerStatistics.getSystemCloseTime().getTime());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeRaw('\n');
    }
}
