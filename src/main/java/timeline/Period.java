package timeline;

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

    public int getTimeInSec() {
        return (int) ((datetimeEnd.getTime() - datetimeStart.getTime()) / 1000);
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", this.getClass().getSimpleName(), datetimeStart, datetimeEnd);
    }
}
