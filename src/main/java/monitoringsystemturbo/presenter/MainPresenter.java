package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.timeline.Timeline;

import java.util.List;

public class MainPresenter {

    @FXML
    private HBox timelineLegend;

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
        addLegendElement(PeriodColor.runningColor, "Running");
        addLegendElement(PeriodColor.activeColor, "Active");
    }

    private void addLegendElement(Color color, String name) {
        Rectangle square = new Rectangle(15, 15);
        square.setArcHeight(5);
        square.setArcWidth(5);
        square.setFill(color);
        Label label = new Label(name);
        label.setPadding(new Insets(0, 5, 0, 0));
        timelineLegend.getChildren().addAll(square, label);
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
