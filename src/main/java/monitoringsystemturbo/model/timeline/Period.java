package monitoringsystemturbo.model.timeline;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Date;

public abstract class Period {

    protected Date datetimeStart;
    protected ObjectProperty<Date> datetimeEnd;

    public Period(Date datetimeStart, Date datetimeEnd) {
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = new SimpleObjectProperty<>(datetimeEnd);
    }

    public Period(Date datetimeStart, ObjectProperty<Date> datetimeEnd) {
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
    }

    public Date getDatetimeStart() {
        return datetimeStart;
    }

    public void setDatetimeStart(Date datetimeStart) {
        this.datetimeStart = datetimeStart;
    }

    public Date getDatetimeEnd() {
        return datetimeEnd.get();
    }

    public void setDatetimeEnd(Date datetimeEnd) {
        this.datetimeEnd.setValue(datetimeEnd);
    }

    public int getTimeInSec(Date fromDate, Date toDate) {
        if (fromDate == null || datetimeStart.getTime() > fromDate.getTime()) {
            fromDate = datetimeStart;
        }
        if (toDate == null || datetimeEnd.getValue().getTime() < toDate.getTime()) {
            toDate = datetimeEnd.getValue();
        }
        return (int) ((toDate.getTime() - fromDate.getTime()) / 1000);
    }

    public ObjectProperty<Date> getDatetimeEndProperty() {
        return this.datetimeEnd;
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", this.getClass().getSimpleName(), datetimeStart, datetimeEnd.getValue());
    }
}
