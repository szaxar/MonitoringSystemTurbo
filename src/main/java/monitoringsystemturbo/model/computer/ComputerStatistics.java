package monitoringsystemturbo.model.computer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatistics {

    protected Date systemStartTime;
    protected Date systemCloseTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

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
        return sdf.format(systemStartTime.getTime());
    }
    @Override
    public String toString() {
        return "monitoringsystemturbo.model.computer.ComputerStatistics{" +
                "systemStartTime=" + sdf.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + sdf.format(systemCloseTime.getTime()) +
                '}';
    }
}
