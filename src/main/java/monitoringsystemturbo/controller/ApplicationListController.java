package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.presenter.AddActivityPresenter;
import monitoringsystemturbo.presenter.AddApplicationPresenter;

import java.io.IOException;

public class ApplicationListController {

    private Stage primaryStage;

    public ApplicationListController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Application showAddView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/addView.fxml"));
        Parent rootLayout = loader.load();

        AddApplicationPresenter addApplicationPresenter;

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add application");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        addApplicationPresenter = loader.getController();
        addApplicationPresenter.setPrimaryStage(dialogStage);

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        Application application = addApplicationPresenter.getApplication();
        return application;

    }

    public void showActivityView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/addActivity.fxml"));
        Parent rootLayout = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add activity");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        AddActivityPresenter addActivityPresenter = loader.getController();
        addActivityPresenter.setPrimaryStage(dialogStage);

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

    }

}
