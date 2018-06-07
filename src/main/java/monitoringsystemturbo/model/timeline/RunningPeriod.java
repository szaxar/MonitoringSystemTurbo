package monitoringsystemturbo.model.timeline;

import javafx.beans.property.ObjectProperty;

import java.util.Date;

public class RunningPeriod extends Period {

    public RunningPeriod(Date datetimeStart, Date datetimeEnd) {
        super(datetimeStart, datetimeEnd);
    }

    public RunningPeriod(Date datetimeStart, ObjectProperty<Date> datetimeEnd) {
        super(datetimeStart, datetimeEnd);
    }

}
