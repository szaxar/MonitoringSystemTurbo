package sample.timeline;

import sample.ApplicationState;

import java.util.ArrayList;
import java.util.Date;

public class Timeline {

    private Date datetimeStart;
    private Date datetimeEnd;
    private ApplicationState lastApplicationState = ApplicationState.NOT_RUNNING;
    private ArrayList<Period> periods = new ArrayList<>();

    public Timeline() {
        this.datetimeStart = new Date();
        this.datetimeEnd = new Date();
    }

    public Timeline(Date datetimeStart, Date datetimeEnd) {
        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
    }

    public void update(Date datetime, ApplicationState state) {
        if (!datetimeEnd.before(datetime)) {
            return;
        }
        datetimeEnd = datetime;
        if (ApplicationState.NOT_RUNNING.equals(state)) {
            lastApplicationState = ApplicationState.NOT_RUNNING;
            return;
        }
        if (!lastApplicationState.equals(state)) {
            Period period;
            switch (state) {
                case RUNNING:
                    period = new RunningPeriod(datetime, datetime);
                    break;
                case ACTIVE:
                    period = new ActivePeriod(datetime, datetime);
                    break;
                default:
                    return;
            }
            periods.add(period);
            lastApplicationState = state;
            return;
        }
        Period lastPeriod = periods.get(periods.size() - 1);
        lastPeriod.setDatetimeEnd(datetime);
    }

    public int getActiveTimeInSec() {
        return 0;
    }

    public int getRunningTimeInSec() {
        return 0;
    }

}
