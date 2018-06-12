package monitoringsystemturbo.controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.presenter.MotivesPresenter;

import java.io.IOException;

public class MotivesController {

    private Stage primaryStage;

    public MotivesController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void showMotivesView(MainController mainController) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/motivesView.fxml"));
        Parent rootLayout = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Motives");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        MotivesPresenter motivesPresenter = loader.getController();
        motivesPresenter.setPrimaryStage(dialogStage);
        motivesPresenter.setMainController(mainController);
        motivesPresenter.reflesh();

        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}