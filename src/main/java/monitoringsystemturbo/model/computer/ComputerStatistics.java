package monitoringsystemturbo.model.computer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatistics {

    protected Date systemStartTime;
    protected Date systemCloseTime;
    protected ObjectProperty<Date> systemCloseTimeProperty;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

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
        return sdf.format(systemStartTime.getTime());
    }

    public int getRunningTimeInSec() {
        return (int) ((systemCloseTime.getTime() - systemStartTime.getTime()) / 1000);
    }

    @Override
    public String toString() {
        return "monitoringsystemturbo.model.computer.ComputerStatistics{" +
                "systemStartTime=" + sdf.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + sdf.format(systemCloseTime.getTime()) +
                '}';
    }
}
