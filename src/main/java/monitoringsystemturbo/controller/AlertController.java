package monitoringsystemturbo.controller;

import javafx.scene.control.Alert;

public class AlertController {

    public static void showAlert(String alertMessage, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.toString()+"!");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

}
