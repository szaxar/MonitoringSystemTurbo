package monitoringsystemturbo.model.computer;

import monitoringsystemturbo.utils.DateFormats;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatistics {

    protected Date systemStartTime;
    protected Date systemCloseTime;

    public ComputerStatistics(Date systemStartTime) {
        this.systemStartTime = systemStartTime;
    }

    public Date getSystemStartTime() {
        return systemStartTime;
    }

    public Date getSystemCloseTime() {
        return systemCloseTime;
    }

    void setSystemCloseTime(Date systemCloseTime) {
        this.systemCloseTime = systemCloseTime;
    }

    public String getSystemStartTimeString(){
        return DateFormats.timeFormat.format(systemStartTime.getTime());
    }

    public int getRunningTimeInSec() {
        return (int) ((systemCloseTime.getTime() - systemStartTime.getTime()) / 1000);
    }

    @Override
    public String toString() {
        return "monitoringsystemturbo.model.computer.ComputerStatistics{" +
                "systemStartTime=" + DateFormats.timeFormat.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + DateFormats.timeFormat.format(systemCloseTime.getTime()) +
                '}';
    }
}
