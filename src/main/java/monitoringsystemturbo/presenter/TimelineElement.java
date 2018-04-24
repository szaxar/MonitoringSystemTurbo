package monitoringsystemturbo.presenter;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.model.timeline.Timeline;

import java.util.List;

public class TimelineElement extends VBox {

    private Label appNameLabel;
    private TimelineView timelineView;

    public TimelineElement(String appName, List<Timeline> timelineModels) throws ClassNotFoundException {
        appNameLabel = createAppNameLabel(appName);
        timelineView = new TimelineView(timelineModels);
        getChildren().addAll(appNameLabel, timelineView);
    }

    private Label createAppNameLabel(String appName) {
        Label label = new Label();
        label.setText(appName);
        label.setPadding(new Insets(3));
        return label;
    }

    public void setTimelineViewWidthByPane(ScrollPane pane) {
        timelineView.widthProperty().bind(pane.widthProperty());
    }

    public void showDay(Integer day) {
        timelineView.showDay(day);
    }

}
