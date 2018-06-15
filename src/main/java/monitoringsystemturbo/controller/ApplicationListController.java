package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.presenter.AddActivityPresenter;
import monitoringsystemturbo.presenter.AddApplicationPresenter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationListController {

    private Stage primaryStage;

    ApplicationListController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Application showAddView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/addApplicationView.fxml"));
        Parent rootLayout = loader.load();

        AddApplicationPresenter addApplicationPresenter;

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add application");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        addApplicationPresenter = loader.getController();
        addApplicationPresenter.setPrimaryStage(dialogStage);
        addApplicationPresenter.reflesh();
        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return addApplicationPresenter.getApplication();
    }

    public Application showActivityView(List<Application> loadedApplications) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/addActivity.fxml"));
        Parent rootLayout = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add activity");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        AddActivityPresenter addActivityPresenter = loader.getController();
        addActivityPresenter.setPrimaryStage(dialogStage);
        addActivityPresenter.reflesh();

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        Date fromDate = addActivityPresenter.getFromDate();
        Date toDate = addActivityPresenter.getToDate();
        Application activity = addActivityPresenter.getActivity();
        if (loadedApplications.contains(activity)) {
            if (activity != null && fromDate != null && toDate != null) {
                Timeline timeline = new Timeline(fromDate, toDate);
                timeline.addPeriod(fromDate, toDate);
                StatisticsManager.save(activity.getName(), timeline);
            }

            return activity;
        }
        if (activity != null && loadedApplications.stream()
                .map(Application::getName)
                .collect(Collectors.toList())
                .contains(activity.getName())) {
            AlertController.showAlert("This name is already used by application.", Alert.AlertType.WARNING);
            return null;
        }
        if (activity != null && fromDate != null && toDate != null) {
            Timeline timeline = new Timeline(fromDate, toDate);
            timeline.addPeriod(fromDate, toDate);
            StatisticsManager.save(activity.getName(), timeline);
        }

        return activity;
    }
}
