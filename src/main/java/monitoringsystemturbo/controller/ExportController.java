package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.exporter.MainExporter;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.presenter.ExportPresenter;

import java.io.IOException;

public class ExportController {

    private Stage primaryStage;

    public ExportController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showExportView(MainExporter mainExporter, TrackingService trackingService) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/exportDateView.fxml"));
        Parent rootLayout = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Export");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        ExportPresenter exportPresenter = loader.getController();
        exportPresenter.setPrimaryStage(dialogStage);
        exportPresenter.setMainExporter(mainExporter);
        exportPresenter.setTrackingService(trackingService);
        exportPresenter.reflesh();

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
