package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainPresenter {

    @FXML private DatePicker datePicker;
    private Integer currentDay;

    @FXML private Legend timelineLegend;
    @FXML private Pane computerTimelineContainer;
    @FXML private ScrollPane appTimelineContainer;
    @FXML private VBox appTimelineList;
    private List<TimelineElement> timelineElements;

    @FXML
    private ListView applicationList;

    @FXML
    public void initialize() {
        renderTimelineLegend();
        initializeTimelines();
        initializeDatePicker();
    }

    private void renderTimelineLegend() {
        timelineLegend.addElement(PeriodColor.runningColor, "Running");
        timelineLegend.addElement(PeriodColor.activeColor, "Active");
    }

    private void initializeTimelines() {
        timelineElements = new ArrayList<>();
        appTimelineContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        try {
            List<ComputerStatistics> computerStatistics = StatisticsManager.loadComputerStats();
            Timeline timeline = new Timeline(computerStatistics);
            TimelineElement timelineElement = new TimelineElement("Computer", Arrays.asList(timeline));
            timelineElement.setTimelineViewWidthByRegion(computerTimelineContainer);
            computerTimelineContainer.getChildren().add(timelineElement);
            timelineElements.add(timelineElement);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<Timeline> timelines = StatisticsManager.load("idea64");
            TimelineElement timelineElement = new TimelineElement("idea64", timelines);
            timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
            appTimelineList.getChildren().add(timelineElement);
            timelineElements.add(timelineElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDatePicker() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Integer day = (int) newValue.toEpochDay();
            timelineElements.forEach(timelineElement -> timelineElement.showDay(day));
            currentDay = day;
        });
        // TODO change to current day
        datePicker.setValue(LocalDate.ofEpochDay(17645)); // 24.04.2018
    }

    @FXML
    public void onPrevDay() {
        datePicker.setValue(LocalDate.ofEpochDay(currentDay - 1));
    }

    @FXML
    public void onNextDay() {
        datePicker.setValue(LocalDate.ofEpochDay(currentDay + 1));
    }

    @FXML
    public void onAddApplication() {}

    @FXML
    public void onRemoveApplication() {}

    @FXML
    public void onExport() {}

}
