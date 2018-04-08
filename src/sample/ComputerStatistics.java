package sample;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComputerStatistics {

    private Calendar systemStartTime;
    private Calendar systemCloseTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public ComputerStatistics(Calendar systemStartTime) {
        this.systemStartTime=systemStartTime;
    }


    public void setSystemCloseTime(Calendar systemCloseTime) {
        this.systemCloseTime = systemCloseTime;
    }

    @Override
    public String toString() {
        return "ComputerStatistics{" +
                "systemStartTime=" +  sdf.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + sdf.format(systemCloseTime.getTime()) +
                '}';
    }
}
