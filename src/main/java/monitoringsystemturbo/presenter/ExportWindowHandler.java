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
    private List<String> applicationsToExport;
    private boolean cancel = true;

    public List<String> displayCheckingWindow(List<String> applicationNames) {
        applicationsToExport = new ArrayList<>();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Export");
        window.setMinWidth(250);
        Label label = new Label();
        label.setText("Choose programs to export");
        List<CheckBox> checkBoxes = createCheckBoxes(applicationNames);
        Button exportButton = new Button("Export");
        exportButton.setOnAction(e -> {
            setSelectedApplications(checkBoxes);
            cancel = false;
            window.close();
        });
        VBox layout = new VBox(10);
        layout.getChildren().add(label);
        for (CheckBox c : checkBoxes)
            layout.getChildren().add(c);
        layout.getChildren().add(exportButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return applicationsToExport;
    }

    private List<CheckBox> createCheckBoxes(List<String> applicationNames) {
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (String app : applicationNames) {
            CheckBox option = new CheckBox(app);
            checkBoxes.add(option);
        }
        return checkBoxes;
    }

    private void setSelectedApplications(List<CheckBox> checkBoxes) {
        for (CheckBox box : checkBoxes)
            if (box.isSelected()) {
                applicationsToExport.add(box.getText());
            }
    }

    public boolean getCancelValue() {
        return this.cancel;
    }
}
