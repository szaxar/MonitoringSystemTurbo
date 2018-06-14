package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.controller.ApplicationListController;
import monitoringsystemturbo.controller.AlertController;
import monitoringsystemturbo.controller.ExportController;
import monitoringsystemturbo.controller.OptionsController;
import monitoringsystemturbo.controller.MainController;
import monitoringsystemturbo.controller.MotivesController;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.ActionsMonitor;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.timeline.PeriodColor;
import monitoringsystemturbo.presenter.timeline.TimelineElement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static monitoringsystemturbo.utils.IconConverter.iconToFxImage;

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
    private JFXDatePicker datePicker;
    @FXML
    private BorderPane borderPane;


    private Integer currentDay;

    private TrackingService trackingService;
    private MainExporter mainExporter;

    private Map<String, TimelineElement> timelineElements;
    private List<Application> loadedApplications;
    private ApplicationListController applicationListController;
    private MotivesController motivesController;
    private ExportController exportController;
    private MainController mainControler;
    private OptionsController optionsController;
    private ActionsMonitor actionsMonitor;

    @FXML
    private ListView<Application> applicationList;

    @FXML
    public void initialize(TrackingService trackingService, MainExporter mainExporter,
                           List<Application> loadedApplications, ActionsMonitor actionsMonitor) {
        currentDay = (int) LocalDate.now().toEpochDay();
        this.trackingService = trackingService;
        this.mainExporter = mainExporter;
        this.loadedApplications = loadedApplications;
        this.actionsMonitor = actionsMonitor;
        setCellFactory();
        applicationList.setItems(FXCollections.observableList(loadedApplications));
        renderTimelineLegend();
        initializeTimelines();
        addCurrentTimeline();
        addCurrentTimelineForComputer();
        initializeDatePicker();
    }

    private void addCurrentTimelineForComputer() {
        TimelineElement timelineElement = this.timelineElements.get("Computer");
        Timeline computerTimeline = new Timeline(trackingService.getComputerStatistics());
        timelineElement.addTimeLineModel(computerTimeline);
    }

    @FXML
    private void addCurrentTimeline() {
        final Map<String, Timeline> allApplicationsStatistics = trackingService.getAllApplicationsStatistics();
        for (String appname : this.timelineElements.keySet()) {
            TimelineElement timelineElement = this.timelineElements.get(appname);
            if (allApplicationsStatistics.get(appname) != null) {
                timelineElement.addTimeLineModel(allApplicationsStatistics.get(appname));
            }
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
            Timeline timeline;
            if (computerStatistics.isEmpty()) {
                timeline = new Timeline();
            } else {
                timeline = new Timeline(computerStatistics);
            }

            TimelineElement timelineElement = new TimelineElement("Computer", Arrays.asList(timeline), currentDay);
            timelineElement.setTimelineViewWidthByRegion(computerTimelineContainer);
            computerTimelineContainer.getChildren().add(timelineElement);
            timelineElements.put("Computer", timelineElement);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (Application application : loadedApplications) {
                ConfigManager.createFileIfNeeded(application);
                List<Timeline> timelines = StatisticsManager.load(application.getName());
                TimelineElement timelineElement = new TimelineElement(application.getName(), timelines, currentDay);
                timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
                appTimelineList.getChildren().add(timelineElement);
                timelineElements.put(application.getName(), timelineElement);
            }
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
        try {
            Application application = applicationListController.showAddView();
            if (application != null) {
                if (loadedApplications.contains(application)) {
                    AlertController.showAlert("Application already exist", Alert.AlertType.INFORMATION);
                    return;
                }
                ConfigManager.createFileIfNeeded(application);
                loadedApplications.add(application);
                ConfigManager.save(loadedApplications);
                trackingService.addAppToMonitor(application.getName());

                applicationList.setItems(FXCollections.observableList(loadedApplications));

                List<Timeline> timelines = StatisticsManager.load(application.getName());
                TimelineElement timelineElement = new TimelineElement(application.getName(), timelines, (int) LocalDate.now().toEpochDay());
                timelineElement.addTimeLineModel(trackingService.getStatisticsForApp(application.getName()));
                timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
                appTimelineList.getChildren().add(timelineElement);
                timelineElements.put(application.getName(), timelineElement);
            }
        } catch (Exception e) {
            AlertController.showAlert("Error occurred while adding application.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onAddActivity() {
        try {
            Application application = applicationListController.showActivityView(loadedApplications);

            if (application != null) {

                ConfigManager.createFileIfNeeded(application);

                if (loadedApplications.contains(application)) {
                    List<Timeline> timelines = StatisticsManager.load(application.getName());
                    TimelineElement timelineElement = timelineElements.get(application.getName());
                    appTimelineList.getChildren().remove(timelineElement);

                    timelineElement = new TimelineElement(application.getName(), timelines, (int) LocalDate.now().toEpochDay());
                    timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
                    timelineElements.put(application.getName(), timelineElement);
                    appTimelineList.getChildren().add(timelineElement);
                    timelineElement.showDay(currentDay);
                    return;
                }

                loadedApplications.add(application);
                applicationList.setItems(FXCollections.observableList(loadedApplications));
                ConfigManager.save(loadedApplications);

                List<Timeline> timelines = StatisticsManager.load(application.getName());
                TimelineElement timelineElement = new TimelineElement(application.getName(), timelines, (int) LocalDate.now().toEpochDay());
                timelineElement.setTimelineViewWidthByRegion(appTimelineContainer);
                appTimelineList.getChildren().add(timelineElement);
                timelineElements.put(application.getName(), timelineElement);
                timelineElement.showDay(currentDay);

            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertController.showAlert("Error occurred while adding an activity.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onRemoveApplication() throws IllegalStateException, IOException {
        Application application = applicationList.getSelectionModel().getSelectedItem();
        if (application != null) {
            try {
                StatisticsManager.save(application.getName(), trackingService.getStatisticsForApp(application.getName()));
            } catch (IllegalStateException e) {
                removeApplicationFromList(application);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!application.getFullPath().equals("")) {
                trackingService.stopAppMonitoring(application.getName());
            }
            removeApplicationFromList(application);
        }
    }

    private void removeApplicationFromList(Application application) {
        loadedApplications.remove(application);
        applicationList.setItems(FXCollections.observableList(loadedApplications));
        try {
            ConfigManager.save(loadedApplications);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TimelineElement timelineElement = timelineElements.values().stream()
                .filter(element -> element.getName().equals(application.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot delete app which are not in list"));

        timelineElements.remove(timelineElement);
        appTimelineList.getChildren().remove(timelineElement);
    }

    @FXML
    public void onExport() {
        try {
            exportController.showExportView(mainExporter, trackingService, loadedApplications);
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error occurred while export an activity.", Alert.AlertType.ERROR);

        }
    }

    @FXML
    public void onOptions() {
        try {
            optionsController.showOptionsView(actionsMonitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCellFactory() {
        applicationList.setCellFactory(param -> new ListCell<Application>() {
            @Override
            public void updateItem(Application application, boolean empty) {
                super.updateItem(application, empty);

                ImageView imageView = new ImageView();
                Label label = new Label();

                VBox vbox = new VBox();

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

    public void setApplicationListController(ApplicationListController applicationListController) {
        this.applicationListController = applicationListController;
    }

    public void setExportController(ExportController exportController) {
        this.exportController = exportController;
    }

    public void setOptionsController(OptionsController optionsController) {
        this.optionsController = optionsController;
    }

    public void setMotivesController(MotivesController motivesController) {
        this.motivesController = motivesController;
    }

    @FXML
    public void onMotives() {
        try {
            motivesController.showMotivesView(mainControler);
        } catch (IOException e) {
            e.printStackTrace();
            AlertController.showAlert("Error occurred while loading motives view.", Alert.AlertType.ERROR);
        }
    }

    public void setMainControler(MainController mainControler) {
        this.mainControler = mainControler;
    }

    public void reflesh() {
        borderPane.setStyle("text-color: #" + MotivesPresenter.textColor.toString().substring(2, 8) + ";" +
                "controller-color: #" + MotivesPresenter.controllerColor.toString().substring(2, 8) + ";" +
                "background-color: #" + MotivesPresenter.backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-color: #" + MotivesPresenter.ripplerColor.toString().substring(2, 8) + ";" +
                "second-color: #" + MotivesPresenter.secondColor.toString().substring(2, 8) + ";");
    }
}

