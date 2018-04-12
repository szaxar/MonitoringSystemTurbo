package sample.timeline;

import java.util.Date;

public abstract class Period {

    protected Date datetimeStart;
    protected Date datetimeEnd;

    public Period(Date datetimeStart, Date datetimeEnd) {
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
        return datetimeEnd;
    }

    public void setDatetimeEnd(Date datetimeEnd) {
        this.datetimeEnd = datetimeEnd;
    }

}
