package monitoringsystemturbo.presenter;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ExportWindowHandler {

    public void displayCheckingWindow(List<String> applicationNames) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Export");
        window.setMinWidth(250);    
        Label label = new Label();
        label.setText("Choose programs to export");

        List<CheckBox> checkBoxes = createCheckBoxes(applicationNames);
        Button closeButton = new Button("Export");
        closeButton.setOnAction(e -> getSelectedApplications(checkBoxes));
        VBox layout = new VBox(10);
        layout.getChildren().add(label);
        for (CheckBox c : checkBoxes)
            layout.getChildren().add(c);
        layout.getChildren().add(closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private List<CheckBox> createCheckBoxes(List<String> applicationNames) {
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (String app : applicationNames) {
            CheckBox option = new CheckBox(app);
            checkBoxes.add(option);
        }
        return checkBoxes;
    }

    private List<String> getSelectedApplications(List<CheckBox> checkBoxes) {
        List<String> selectedApplications = new ArrayList<>();
        for (CheckBox box : checkBoxes)
            if (box.isSelected()) {
                selectedApplications.add(box.getText());
                System.out.println(box.getText());
            }
        return selectedApplications;
    }
}
