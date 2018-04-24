package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.IOException;
import java.util.List;

public class MainPresenter {

    @FXML
    private Pane computerTimelineContainer;

    @FXML
    private ScrollPane appTimelineContainer;
    @FXML
    private VBox appTimelineList;

    @FXML
    private ListView applicationList;

    @FXML
    public void initialize() {
        initializeTimelines();
    }

    private void initializeTimelines() {
        appTimelineContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        try {
            List<Timeline> timelines = StatisticsManager.load("idea64");

            TimelineElement timelineElement = new TimelineElement("idea64", timelines);
            timelineElement.setTimelineViewWidthByPane(appTimelineContainer);
            appTimelineList.getChildren().add(timelineElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddApplication() {}

    @FXML
    public void onRemoveApplication() {}

    @FXML
    public void onExport() {}

}
