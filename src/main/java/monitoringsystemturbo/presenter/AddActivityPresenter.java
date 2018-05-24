package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import monitoringsystemturbo.model.app.Application;

public class AddActivityPresenter {

    private Stage primaryStage;
    @FXML
    private TextField nameActivity;
    @FXML
    private TextField startTime;
    @FXML
    private TextField duration;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void onAdd() {
    }

    public void onCancel() {
        primaryStage.close();
    }

}
