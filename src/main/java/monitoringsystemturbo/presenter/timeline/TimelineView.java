package monitoringsystemturbo.presenter.timeline;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import monitoringsystemturbo.model.timeline.ActivePeriod;
import monitoringsystemturbo.model.timeline.Period;
import monitoringsystemturbo.model.timeline.RunningPeriod;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.utils.DateFormats;

import java.time.LocalDate;
import java.util.*;

import static monitoringsystemturbo.presenter.timeline.PeriodColor.*;

public class TimelineView extends Group {

    private final static int height = 35;
    private final static long dayInMs = 24 * 60 * 60 * 1000;
    private final static long hInMs = 60 * 60 * 1000;
    private final static long dateOffset = hInMs + TimeZone.getDefault().getRawOffset();

    private Rectangle timelineBackground;
    private HashMap<Integer, List<Node>> dayPeriodsMap = new HashMap<>();
    private Integer currentDay;
    private Rectangle currentPeriodView;

    TimelineView(List<Timeline> timelineModels, Integer day) throws ClassNotFoundException {
        currentDay = day;
        setTimelineBackground();
        for (Timeline timeline : timelineModels) {
            renderPeriods(timeline.getPeriods());
        }
    }

    private void setTimelineBackground() {
        timelineBackground = new Rectangle();
        timelineBackground.setFill(notRunningColor);
        timelineBackground.setHeight(height);
        getChildren().add(timelineBackground);
    }

    private void renderPeriods(List<Period> periods) throws ClassNotFoundException {
        for (Period period : periods) {
            renderPeriod(period);
        }
    }

    private void renderPeriod(Period period) throws ClassNotFoundException {
        long datetimeStart = period.getDatetimeStart().getTime();
        long datetimeEnd = period.getDatetimeEnd().getTime();
        for (int day = (int) ((datetimeStart + dateOffset) / dayInMs); day <= (datetimeEnd + dateOffset) / dayInMs; day++) {
            Rectangle periodView = createPeriodView(period);
            this.currentPeriodView = periodView;
            long datetimeStartInDay = Math.max(datetimeStart, day * dayInMs - dateOffset);
            long datetimeEndInDay = Math.min(datetimeEnd, (day + 1) * dayInMs - dateOffset - 1);
            setPeriodViewWidthProperties(periodView, datetimeStartInDay, datetimeEndInDay);
            addPeriodViewForDay(day, periodView);
        }
    }

    private Rectangle createPeriodView(Period period) throws ClassNotFoundException {
        Rectangle periodView = new Rectangle();
        setPeriodColor(periodView, period);
        periodView.heightProperty().bind(heightProperty());
        setPeriodTooltip(periodView, period);
        return periodView;
    }

    private void setPeriodColor(Rectangle periodView, Period period) throws ClassNotFoundException {
        if (period instanceof RunningPeriod) {
            periodView.setFill(runningColor);
        } else if (period instanceof ActivePeriod) {
            periodView.setFill(activeColor);
        } else {
            throw new ClassNotFoundException("Unrecognized period instance");
        }
    }

    private void setPeriodTooltip(Rectangle periodView, Period period) {
        StringBuilder tooltipContent = new StringBuilder();
        tooltipContent
                .append(DateFormats.datetimeFormat.format(period.getDatetimeStart()))
                .append(" - ");
        if (period.getDatetimeStart().getTime() / dayInMs == period.getDatetimeEnd().getTime() / dayInMs) {
            tooltipContent.append(DateFormats.timeFormat.format(period.getDatetimeEnd()));
        } else {
            tooltipContent.append(DateFormats.datetimeFormat.format(period.getDatetimeEnd()));
        }
        Tooltip tooltip = new Tooltip(tooltipContent.toString());
        Tooltip.install(periodView, tooltip);
    }

    private void setPeriodViewWidthProperties(Rectangle periodView, long datetimeStart, long datetimeEnd) {
        double widthRatio = (double) (datetimeEnd - datetimeStart) / dayInMs;
        double offsetRatio = (double) ((datetimeStart + dateOffset) % dayInMs) / dayInMs;
        widthProperty().addListener((observable, oldValue, newValue) -> {
            double timelineWidth = newValue.doubleValue();
            periodView.setWidth(timelineWidth * widthRatio);
            periodView.setTranslateX(timelineWidth * offsetRatio);
        });
    }

    private void addPeriodViewForDay(int day, Node periodView) {
        if (!dayPeriodsMap.containsKey(day)) {
            dayPeriodsMap.put(day, new ArrayList<>());
        }
        dayPeriodsMap.get(day).add(periodView);
    }

    DoubleProperty widthProperty() {
        return timelineBackground.widthProperty();
    }

    private DoubleProperty heightProperty() {
        return timelineBackground.heightProperty();
    }

    void showDay(Integer day) {
        if(dayPeriodsMap.containsKey(currentDay))
            getChildren().removeAll(dayPeriodsMap.get(currentDay));
        if(dayPeriodsMap.containsKey(day))
            getChildren().addAll(dayPeriodsMap.get(day));
        currentDay = day;
    }

    void addTimeLine(Timeline timeLine) {
        if (!timeLine.getPeriods().isEmpty()) {
            try {
                Period period = timeLine.getPeriods().get(timeLine.getPeriods().size() - 1);
                renderPeriod(period);
                setListenerForPeriod(period);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        timeLine.getPeriods().addListener(this::onNewPeriodAdded);
    }

    private void setListenerForPeriod(Period period) {
        period.getDatetimeEndProperty().addListener((observable, oldValue, newValue) -> {
            double widthRatio = (double) (newValue.getTime() - period.getDatetimeStart().getTime()) / dayInMs;
            double offsetRatio = (double) ((period.getDatetimeStart().getTime() + dateOffset) % dayInMs) / dayInMs;
            double timelineWidth = widthProperty().getValue();
            currentPeriodView.setWidth(timelineWidth * widthRatio);
            currentPeriodView.setTranslateX(timelineWidth * offsetRatio);
            setPeriodTooltip(currentPeriodView, period);
        });
    }

    private void onNewPeriodAdded(ListChangeListener.Change<? extends Period> change) {

        ObservableList<? extends Period> list = change.getList();
        Period period = list.get(list.size() - 1);

        try {
            renderPeriod(period);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        setListenerForPeriod(period);

        Integer todayInMilis = (int) LocalDate.now().toEpochDay();
        if (currentDay.equals(todayInMilis)) {
            Platform.runLater(() -> {
                if(dayPeriodsMap.containsKey(currentDay))
                    getChildren().removeAll(dayPeriodsMap.get(currentDay));
                if(dayPeriodsMap.containsKey(todayInMilis))
                    getChildren().addAll(dayPeriodsMap.get(todayInMilis));
            });
        }

    }
}
