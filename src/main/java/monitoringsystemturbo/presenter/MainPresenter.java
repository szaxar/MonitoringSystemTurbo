package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.timeline.PeriodColor;
import monitoringsystemturbo.presenter.timeline.TimelineElement;

import java.util.List;

public class MainPresenter {

    @FXML
    private Legend timelineLegend;

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
        renderTimelineLegend();
        initializeTimelines();
    }

    private void renderTimelineLegend() {
        timelineLegend.addElement(PeriodColor.runningColor, "Running");
        timelineLegend.addElement(PeriodColor.activeColor, "Active");
    }

    private void initializeTimelines() {
        appTimelineContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        try {
            List<Timeline> timelines = StatisticsManager.load("idea64");

            TimelineElement timelineElement = new TimelineElement("idea64", timelines);
            timelineElement.setTimelineViewWidthByPane(appTimelineContainer);
            appTimelineList.getChildren().add(timelineElement);
            timelineElement.showDay(17645); // 24.04.2018
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
