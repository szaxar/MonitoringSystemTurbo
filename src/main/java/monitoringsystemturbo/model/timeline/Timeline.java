package monitoringsystemturbo.model.timeline;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import monitoringsystemturbo.model.OnTimeLineChangerListener;
import monitoringsystemturbo.model.app.ApplicationState;
import monitoringsystemturbo.model.computer.ComputerStatistics;

import java.util.*;

public class Timeline {

    protected Date datetimeStart;
    protected Date datetimeEnd;
    private ApplicationState lastApplicationState = ApplicationState.NOT_RUNNING;
    protected ObservableList<Period> observableList;

    public Timeline() {
        this(new Date(), new Date());
    }

    public Timeline(Date datetimeStart, Date datetimeEnd) {
        observableList = FXCollections.observableArrayList();

        this.datetimeStart = datetimeStart;
        this.datetimeEnd = datetimeEnd;
    }

    public Timeline(List<ComputerStatistics> computerStatistics) {
        observableList = FXCollections.observableArrayList();
        this.datetimeStart = computerStatistics.get(0).getSystemStartTime();
        this.datetimeEnd = computerStatistics.get(0).getSystemCloseTime();
        for (ComputerStatistics computerStatistic : computerStatistics) {
            Date datetimeStart = computerStatistic.getSystemStartTime();
            Date datetimeEnd = computerStatistic.getSystemCloseTime();
            if (datetimeStart.getTime() < this.datetimeStart.getTime()) {
                this.datetimeStart = computerStatistic.getSystemStartTime();
            }
            if (datetimeEnd.getTime() > this.datetimeEnd.getTime()) {
                this.datetimeEnd = computerStatistic.getSystemCloseTime();
            }
            Period period = new RunningPeriod(datetimeStart, datetimeEnd);
            observableList.add(period);
        }
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
            observableList.add(period);
            lastApplicationState = state;
            return;
        }
        Period lastPeriod = observableList.get(observableList.size() - 1);
        lastPeriod.setDatetimeEnd(datetime);
    }

    public Date getDatetimeStart() {
        return datetimeStart;
    }

    public Date getDatetimeEnd() {
        return datetimeEnd;
    }

    public ObservableList<Period> getPeriods() {
        return observableList;
    }

    public int getActiveTimeInSec() {
        int timeInSec = 0;
        for (Period period : observableList) {
            if (period instanceof ActivePeriod) {
                timeInSec += period.getTimeInSec();
            }
        }
        return timeInSec;
    }

    public int getRunningTimeInSec() {
        int timeInSec = 0;
        for (Period period : observableList) {
            if (period instanceof RunningPeriod) {
                timeInSec += period.getTimeInSec();
            }
        }
        return timeInSec;
    }

    @Override
    public String toString() {
        return String.format("Timeline(%s, %s, %s)", datetimeStart, datetimeEnd, observableList);
    }
}
