package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.presenter.ConfirmExportPresenter;

import java.io.IOException;
import java.util.List;

public class ConfirmExportController {
    private Stage primaryStage;
    private ConfirmExportPresenter confirmExportPresenter;

    public ConfirmExportController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public List<String> showConfirmationAndGetAppList(List<String> applications)
            throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(this.getClass().getResource("/confirmExportView.fxml"));
        Parent rootLayout = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Confirm export");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        confirmExportPresenter = loader.getController();
        confirmExportPresenter.setPrimaryStage(dialogStage);
        confirmExportPresenter.setApplicationsNames(applications);
        confirmExportPresenter.reflesh();
        confirmExportPresenter.start();

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return confirmExportPresenter.getSelectedApplications();
    }

    public boolean getCancelValue() {
        return confirmExportPresenter.wasCancelled();
    }
}
