package monitoringsystemturbo.controller;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorController {

    public static void showError(String errorMessage) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.setMinWidth(350);

        Label label = new Label();
        label.setText(errorMessage);
        label.setPadding(new Insets(20, 10, 20, 10));

        Group root = new Group();
        root.getChildren().add(label);
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }

}
