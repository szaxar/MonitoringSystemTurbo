package monitoringsystemturbo.presenter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.OnTimeLineChangerListener;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Period;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.timeline.PeriodColor;
import monitoringsystemturbo.presenter.timeline.TimelineElement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static monitoringsystemturbo.utils.IconConverter.iconToFxImage;

public class MainPresenter implements OnTimeLineChangerListener {

    @FXML
    private Legend timelineLegend;
    @FXML
    private Pane computerTimelineContainer;
    @FXML
    private ScrollPane appTimelineContainer;
    @FXML
    private VBox appTimelineList;
    @FXML
    private DatePicker datePicker;

    private TrackingService trackingService;
    private MainExporter mainExporter;
    private Integer currentDay;

    private Map<String, TimelineElement> timelineElements;
    private List<Application> loadedApplications;

    @FXML
    private ListView<Application> applicationList;

    @FXML
    public void initialize(List<Application> loadedApplications) {

        mainExporter = new MainExporter();
        trackingService = new TrackingService(this);
        this.loadedApplications = loadedApplications;
        initializeAppsToMonitor();
        trackingService.start();

        applicationList.setItems(FXCollections.observableList(loadedApplications));
        setCellFactory();
        renderTimelineLegend();
        initializeTimelines();
        initializeDatePicker();

    }

    private void initializeAppsToMonitor() {
        for (Application application : loadedApplications) {
            trackingService.addAppToMonitor(application.getName());
        }
    }

    private void renderTimelineLegend() {
        timelineLegend.addElement(PeriodColor.runningColor, "Running");
        timelineLegend.addElement(PeriodColor.activeColor, "Active");
    }

    private void initializeTimelines() {
        timelineElements = new HashMap<>();
        appTimelineContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        try {
            List<ComputerStatistics> computerStatistics = StatisticsManager.loadComputerStats();
            Timeline timeline = new Timeline(computerStatistics);
            TimelineElement timelineElement = new TimelineElement("Computer", Arrays.asList(timeline));
            timelineElement.setTimelineViewWidthByRegion(computerTimelineContainer);
            computerTimelineContainer.getChildren().add(timelineElement);
            timelineElements.put("Computer", timelineElement);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<Timeline> timelines = StatisticsManager.load("chrome");
            TimelineElement timelineElement = new TimelineElement("chrome", timelines);
            timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
            appTimelineList.getChildren().add(timelineElement);
            timelineElements.put("chrome", timelineElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDatePicker() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Integer day = (int) newValue.toEpochDay();
            timelineElements.values().forEach(timelineElement -> timelineElement.showDay(day));
            currentDay = day;
        });
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void onPrevDay() {
        datePicker.setValue(LocalDate.ofEpochDay(currentDay - 1));
    }

    @FXML
    public void onNextDay() {
        if (!LocalDate.ofEpochDay(currentDay).equals(LocalDate.now()))
            datePicker.setValue(LocalDate.ofEpochDay(currentDay + 1));
    }

    @FXML
    public void onAddApplication() {
    }

    @FXML
    public void onRemoveApplication() {
        //pls remember to save statistics, otherwise data will be lost!
    }

    @FXML
    public void onExport() {
        ExportWindowHandler exportWindowHandler = new ExportWindowHandler();
        List<String> applicationsToExport = exportWindowHandler.displayCheckingWindow(trackingService.getApplicationsNames());
        if (!exportWindowHandler.getCancelValue()) {
            try {
                mainExporter.export(trackingService, applicationsToExport);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Data exported successfully! ");
                successAlert.showAndWait();
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Error occurred while exporting data.");
                errorAlert.showAndWait();
            }
        }
    }

    private void setCellFactory() {
        applicationList.setCellFactory(param -> new ListCell<Application>() {
            private ImageView imageView = new ImageView();
            private Label label = new Label();
            private VBox vbox = new VBox();

            @Override
            public void updateItem(Application application, boolean empty) {
                super.updateItem(application, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Image image = iconToFxImage(application.findIcon());
                    label.setText(application.getName());
                    imageView.setImage(image);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.getChildren().add(imageView);
                    vbox.getChildren().add(label);
                    setGraphic(vbox);
                }
            }
        });
    }

    @Override
    public void onTimelineChange(List<Period> periods, String appName) {
        if (timelineElements != null) {
            System.out.println(periods + " " + appName);
            TimelineElement element = timelineElements.get(appName);
            try {
                element.getTimeLineView().renderPeriods(periods);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
