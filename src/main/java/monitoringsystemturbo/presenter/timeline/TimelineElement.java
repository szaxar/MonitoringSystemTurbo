package monitoringsystemturbo.presenter.timeline;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.model.timeline.Timeline;

import java.util.List;

public class TimelineElement extends VBox {

    private Label appNameLabel;
    private TimelineView timelineView;

    public TimelineElement(String appName, List<Timeline> timelineModels, Integer day) throws ClassNotFoundException {
        appNameLabel = createAppNameLabel(appName);
        timelineView = new TimelineView(timelineModels,  day);
        getChildren().addAll(appNameLabel, timelineView);
    }

    private Label createAppNameLabel(String appName) {
        Label label = new Label();
        label.setText(appName);
        label.setPadding(new Insets(3));
        return label;
    }

    public void setTimelineViewWidthByRegion(Region region) {
        timelineView.widthProperty().bind(region.widthProperty());
    }

    public TimelineView getTimeLineView(){
        return timelineView;
    }

    public void showDay(Integer day) {
        timelineView.showDay(day);
    }

    public void addTimeLineModel(Timeline timeLine) {
        timelineView.addTimeLine(timeLine);
    }

    public String getName() {
        return appNameLabel.getText();
    }
}
