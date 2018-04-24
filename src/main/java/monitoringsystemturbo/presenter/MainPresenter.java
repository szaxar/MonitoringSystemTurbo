package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.timeline.PeriodColor;
import monitoringsystemturbo.presenter.timeline.TimelineElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

        int day = 17645; // 24.04.2018

        try {
            List<ComputerStatistics> computerStatistics = StatisticsManager.loadComputerStats();
            Timeline timeline = new Timeline(computerStatistics);
            TimelineElement timelineElement = new TimelineElement("Computer", Arrays.asList(timeline));
            timelineElement.setTimelineViewWidthByRegion(computerTimelineContainer);
            computerTimelineContainer.getChildren().add(timelineElement);
            timelineElement.showDay(day);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<Timeline> timelines = StatisticsManager.load("idea64");
            TimelineElement timelineElement = new TimelineElement("idea64", timelines);
            timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
            appTimelineList.getChildren().add(timelineElement);
            timelineElement.showDay(day);
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
