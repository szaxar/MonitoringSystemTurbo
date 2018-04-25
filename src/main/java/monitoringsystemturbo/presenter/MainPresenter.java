package monitoringsystemturbo.presenter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.timeline.PeriodColor;
import monitoringsystemturbo.presenter.timeline.TimelineElement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.app.Application;

import java.io.IOException;
import java.util.List;

import static monitoringsystemturbo.utils.IconConverter.iconToFxImage;

public class MainPresenter {
    private TrackingService trackingService;
    private MainExporter mainExporter;

    private List<Application> loadedApplications;
    @FXML private DatePicker datePicker;
    private Integer currentDay;

    @FXML private Legend timelineLegend;
    @FXML private Pane computerTimelineContainer;
    @FXML private ScrollPane appTimelineContainer;
    @FXML private VBox appTimelineList;
    private List<TimelineElement> timelineElements;

    @FXML
    private ListView<Application> applicationList;

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
            List<Timeline> timelines = StatisticsManager.load("chrome");
            TimelineElement timelineElement = new TimelineElement("chrome", timelines);
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
        datePicker.setValue(LocalDate.now()); // 24.04.2018
    }

    @FXML
    public void onPrevDay() {
        datePicker.setValue(LocalDate.ofEpochDay(currentDay - 1));
    }

    @FXML
    public void onNextDay() {
        datePicker.setValue(LocalDate.ofEpochDay(currentDay + 1));
    }
    public void initialize(List<Application> loadedApplications) {
        mainExporter = new MainExporter();

        trackingService = new TrackingService();
        trackingService.addAppToMonitor("idea64");  //just hardcoded for now, we'll change it
        trackingService.addAppToMonitor("chrome");
        trackingService.start();

        this.loadedApplications = loadedApplications;
        applicationList.setItems(FXCollections.observableList(loadedApplications));
        setCellFactory();

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

}
