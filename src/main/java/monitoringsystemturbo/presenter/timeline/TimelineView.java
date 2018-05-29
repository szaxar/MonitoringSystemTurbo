package monitoringsystemturbo.presenter.timeline;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;
import monitoringsystemturbo.model.timeline.ActivePeriod;
import monitoringsystemturbo.model.timeline.Period;
import monitoringsystemturbo.model.timeline.RunningPeriod;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.utils.DateFormats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static monitoringsystemturbo.presenter.timeline.PeriodColor.*;

public class TimelineView extends Group {

    private final static int height = 35;
    private final static long dayInMs = 24 * 60 * 60 * 1000;

    private Rectangle timelineBackground;
    private List<Timeline> timelineModels;
    private HashMap<Integer, List<Node>> dayPeriodsMap = new HashMap<>();
    private Integer currentDay = null;

    public TimelineView(List<Timeline> timelineModels) throws ClassNotFoundException {
        this.timelineModels = timelineModels;
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
            long datetimeStart = period.getDatetimeStart().getTime();
            long datetimeEnd = period.getDatetimeEnd().getTime();
            for (int day = (int)(datetimeStart / dayInMs); day <= datetimeEnd / dayInMs; day++) {
                Rectangle periodView = createPeriodView(period);
                long datetimeStartInDay = Math.max(datetimeStart, day * dayInMs);
                long datetimeEndInDay = Math.min(datetimeEnd, (day + 1) * dayInMs);
                setPeriodViewWidthProperties(periodView, datetimeStartInDay, datetimeEndInDay);
                addPeriodViewForDay(day, periodView);
            }
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
        double offsetRatio = (double) (datetimeStart % dayInMs) / dayInMs;
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

    public DoubleProperty widthProperty() {
        return timelineBackground.widthProperty();
    }

    public DoubleProperty heightProperty() {
        return timelineBackground.heightProperty();
    }

    public void showDay(Integer day) {
        if (currentDay != null && currentDay.equals(day)) {
            return;
        }
        if (currentDay != null) {
            getChildren().removeAll(dayPeriodsMap.get(currentDay));
        }
        if (day == null || !dayPeriodsMap.containsKey(day)) {
            currentDay = null;
            return;
        }
        getChildren().addAll(dayPeriodsMap.get(day));
        currentDay = day;
    }

}
