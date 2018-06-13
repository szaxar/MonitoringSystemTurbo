package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.model.ActionsMonitor;
import monitoringsystemturbo.presenter.OptionsPresenter;

import java.io.IOException;

public class OptionsController {
    private Stage primaryStage;
    private ActionsMonitor actionsMonitor;

    public OptionsController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showOptionsView(ActionsMonitor actionsMonitor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/optionsView.fxml"));
        Parent rootLayout = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Options");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        OptionsPresenter optionsPresenter = loader.getController();
        optionsPresenter.setPrimaryStage(dialogStage);
        optionsPresenter.setActionsMonitor(actionsMonitor);
        optionsPresenter.start();

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
