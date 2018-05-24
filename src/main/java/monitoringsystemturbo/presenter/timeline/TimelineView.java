package monitoringsystemturbo.presenter.timeline;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.ActivePeriod;
import monitoringsystemturbo.model.timeline.Period;
import monitoringsystemturbo.model.timeline.RunningPeriod;
import monitoringsystemturbo.model.timeline.Timeline;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static monitoringsystemturbo.presenter.timeline.PeriodColor.*;

public class TimelineView extends Group {

    private final static int height = 35;
    private final static long dayInMs = 24 * 60 * 60 * 1000;

    private Rectangle timelineBackground;
    private List<Timeline> timelineModels;
    private HashMap<Integer, List<Node>> dayPeriodsMap = new HashMap<>();
    private Integer currentDay;
    private Rectangle currentPeriodView;

    TimelineView(List<Timeline> timelineModels) throws ClassNotFoundException {
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
            renderPeriod(period);
        }
    }

    private void renderPeriod(Period period) throws ClassNotFoundException {
        long datetimeStart = period.getDatetimeStart().getTime();
        long datetimeEnd = period.getDatetimeEnd().getTime();
        for (int day = (int) (datetimeStart / dayInMs); day <= datetimeEnd / dayInMs; day++) {
            Rectangle periodView = createPeriodView(period);
            this.currentPeriodView = periodView;
            long datetimeStartInDay = Math.max(datetimeStart, day * dayInMs);
            long datetimeEndInDay = Math.min(datetimeEnd, (day + 1) * dayInMs);
            setPeriodViewWidthProperties(periodView, datetimeStartInDay, datetimeEndInDay);
            addPeriodViewForDay(day, periodView);
        }
    }

    private Rectangle createPeriodView(Period period) throws ClassNotFoundException {
        Rectangle periodView = new Rectangle();
        if (period instanceof RunningPeriod) {
            periodView.setFill(runningColor);
        } else if (period instanceof ActivePeriod) {
            periodView.setFill(activeColor);
        } else {
            throw new ClassNotFoundException("Unrecognized period instance");
        }
        periodView.heightProperty().bind(heightProperty());
        return periodView;
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

    DoubleProperty widthProperty() {
        return timelineBackground.widthProperty();
    }

    private DoubleProperty heightProperty() {
        return timelineBackground.heightProperty();
    }

    void showDay(Integer day) {
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

    void addTimeLine(Timeline timeLine) {

        System.out.println(timeLine);
        if (!timeLine.getPeriods().isEmpty()) {
            try {
                Period period = timeLine.getPeriods().get(timeLine.getPeriods().size() - 1);
                renderPeriod(period);
                setListenerForPeriod(period);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        timeLine.getPeriods().addListener((ListChangeListener<Period>) this::onChanged);

    }

    private void setListenerForPeriod(Period period) {
        period.getgetDatetimeEndProperty().addListener((observable, oldValue, newValue) -> {
            double widthRatio = (double) (newValue.getTime() - period.getDatetimeStart().getTime()) / dayInMs;
            double offsetRatio = (double) (period.getDatetimeStart().getTime() % dayInMs) / dayInMs;
            double timelineWidth = widthProperty().getValue();
            currentPeriodView.setWidth(timelineWidth * widthRatio);
            currentPeriodView.setTranslateX(timelineWidth * offsetRatio);
        });
    }

    private void onChanged(ListChangeListener.Change<? extends Period> c) {
        Integer todayInMilis = (int) LocalDate.now().toEpochDay();
        if (currentDay != null && currentDay.equals(todayInMilis)) {
            Platform.runLater(() -> {
                getChildren().removeAll(dayPeriodsMap.get(currentDay));
                getChildren().addAll(dayPeriodsMap.get(todayInMilis));
            });
        }
        ObservableList<? extends Period> list = c.getList();
        Period period = list.get(list.size() - 1);

        try {
            renderPeriod(period);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        setListenerForPeriod(period);
    }
}
