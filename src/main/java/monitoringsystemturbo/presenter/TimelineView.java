package monitoringsystemturbo.presenter;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import monitoringsystemturbo.model.timeline.ActivePeriod;
import monitoringsystemturbo.model.timeline.Period;
import monitoringsystemturbo.model.timeline.RunningPeriod;
import monitoringsystemturbo.model.timeline.Timeline;

import java.util.List;

public class TimelineView extends Group {

    private final static int height = 35;
    private final static Color notRunningColor = Color.web("0x696969");
    private final static Color runningColor = Color.web("0x0084ff");
    private final static Color activeColor = Color.web("0x65ff00");

    private final static long dayInMs = 24 * 60 * 60 * 1000;

    private Rectangle timelineBackground;
    private List<Timeline> timelineModels;

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
            Rectangle periodView = createPeriod(period);
            getChildren().add(periodView);
        }
    }

    private Rectangle createPeriod(Period period) throws ClassNotFoundException {
        Rectangle periodView = new Rectangle();
        if (period instanceof RunningPeriod) {
            periodView.setFill(runningColor);
        } else if (period instanceof ActivePeriod) {
            periodView.setFill(activeColor);
        } else {
            throw new ClassNotFoundException("Unrecognized period instance");
        }
        periodView.heightProperty().bind(heightProperty());

        double widthRatio = (double) period.getTimeInMs() / dayInMs;
        double offsetRatio = (double) (period.getDatetimeStart().getTime() % dayInMs) / dayInMs;
        widthProperty().addListener((observable, oldValue, newValue) -> {
            double timelineWidth = newValue.doubleValue();
            periodView.setWidth(timelineWidth * widthRatio);
            periodView.setTranslateX(timelineWidth * offsetRatio);
        });
        return periodView;
    }

    public DoubleProperty widthProperty() {
        return timelineBackground.widthProperty();
    }

    public DoubleProperty heightProperty() {
        return timelineBackground.heightProperty();
    }

}
