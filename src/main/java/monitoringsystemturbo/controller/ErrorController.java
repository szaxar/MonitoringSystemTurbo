package monitoringsystemturbo.controller;

import javafx.scene.control.Alert;

public class ErrorController {

    public static void showAlert(String errorMessage, Alert.AlertType type) {
        Alert errorAlert = new Alert(type);
        errorAlert.setTitle(type.toString()+"!");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(errorMessage);
        errorAlert.showAndWait();
    }

}
