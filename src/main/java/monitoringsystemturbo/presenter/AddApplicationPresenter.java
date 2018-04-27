package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import monitoringsystemturbo.model.app.Application;

public class AddApplicationPresenter {

    @FXML
    private Button addButton;
    private Stage primaryStage;
    @FXML
    private TextField nameApplication;
    @FXML
    private TextField fullPathApplication;
    private Application application;
    private Boolean isCancel=false;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Application onAdd(){
        application=new Application(nameApplication.getText(),fullPathApplication.getText());
        primaryStage.close();
        return application;
    }

    public void onCancel(){
        isCancel=true;
        primaryStage.close();
    }

    public Boolean getCancel() {
        return isCancel;
    }
}
