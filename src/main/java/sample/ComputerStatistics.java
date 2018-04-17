package sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatistics {

    private Date systemStartTime;
    private Date systemCloseTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public ComputerStatistics(Date systemStartTime) {
        this.systemStartTime = systemStartTime;
    }

    public void setSystemCloseTime(Date systemCloseTime) {
        this.systemCloseTime = systemCloseTime;
    }

    @Override
    public String toString() {
        return "ComputerStatistics{" +
                "systemStartTime=" + sdf.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + sdf.format(systemCloseTime.getTime()) +
                '}';
    }
}
