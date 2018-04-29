package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.presenter.AddApplicationPresenter;

import java.io.IOException;

public class AddApplicationController {

    private Stage primaryStage;
    private AddApplicationPresenter addApplicationPresenter;
    public AddApplicationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showAddView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/addView.fxml"));
        Parent rootLayout = loader.load();



        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add application");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        addApplicationPresenter=loader.getController();
        addApplicationPresenter.setPrimaryStage(dialogStage);

        Scene scene=new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public Application getNewApplication(){
        Application application= addApplicationPresenter.getApplication();
        return application;
    }

}
