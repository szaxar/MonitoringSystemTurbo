package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage primaryStage;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMainWindow() throws IOException {
        Parent rootLayout = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();
    }

}
