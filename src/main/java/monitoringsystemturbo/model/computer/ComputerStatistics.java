package monitoringsystemturbo.model.computer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import monitoringsystemturbo.utils.DateFormats;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatistics {

    protected Date systemStartTime;
    protected Date systemCloseTime;
    protected ObjectProperty<Date> systemCloseTimeProperty;

    public ComputerStatistics(Date systemStartTime) {
        this.systemStartTime = systemStartTime;
        systemCloseTimeProperty = new SimpleObjectProperty<>(systemStartTime);
    }

    public Date getSystemStartTime() {
        return systemStartTime;
    }

    public Date getSystemCloseTime() {
        return systemCloseTime;
    }

    void setSystemCloseTime(Date systemCloseTime) {
        this.systemCloseTime = systemCloseTime;
        this.systemCloseTimeProperty.setValue(systemCloseTime);
    }

    public ObjectProperty<Date> getSystemCloseTimeProperty() {
        return systemCloseTimeProperty;
    }

    public String getSystemStartTimeString(){
        return DateFormats.timeFormat.format(systemStartTime.getTime());
    }

    public int getRunningTimeInSec() {
        return getRunningTimeInSec(null, null);
    }

    public int getRunningTimeInSec(Date fromDate, Date toDate) {
        if (fromDate == null || systemStartTime.getTime() > fromDate.getTime()) {
            fromDate = systemStartTime;
        }
        if (toDate == null || systemCloseTime.getTime() < toDate.getTime()) {
            toDate = systemCloseTime;
        }
        return (int) ((toDate.getTime() - fromDate.getTime()) / 1000);
    }

    @Override
    public String toString() {
        return "monitoringsystemturbo.model.computer.ComputerStatistics{" +
                "systemStartTime=" + DateFormats.timeFormat.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + DateFormats.timeFormat.format(systemCloseTime.getTime()) +
                '}';
    }
}
