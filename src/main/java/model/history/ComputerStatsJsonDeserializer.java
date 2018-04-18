package model.history;

import com.fasterxml.jackson.databind.JsonNode;
import model.computer.ComputerStatistics;

import java.util.Date;

public class ComputerStatsJsonDeserializer extends ComputerStatistics {

    public ComputerStatsJsonDeserializer(JsonNode computerStatisticsJson) {
        super(new Date(computerStatisticsJson.get("systemStartTime").asLong()));
        this.systemCloseTime = new Date(computerStatisticsJson.get("systemCloseTime").asLong());
    }

    public ComputerStatistics getComputerStatistics() {
        return this;
    }

}
