package monitoringsystemturbo.presenter;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfirmExportPresenter {

    @FXML
    private VBox checkBoxesContainer;

    @FXML
    private BorderPane borderPane;

    private Stage primaryStage;
    private List<String> applicationsNames;

    private List<JFXCheckBox> checkBoxes = new ArrayList<>();
    private List<String> selectedApplications = new ArrayList<>();
    private boolean cancelled = true;

    @FXML
    public void onConfirm() {
        setSelectedApplications(checkBoxes);
        cancelled = false;
        primaryStage.close();
    }

    @FXML
    public void onCancel() {
        primaryStage.close();
    }

    @FXML
    public void onCheckChange() {
        setCheckboxes();
    }

    public void start() {
        initializeCheckBoxes(applicationsNames);
        for (JFXCheckBox checkBox : checkBoxes) {
            checkBoxesContainer.getChildren().add(checkBox);
        }
    }

    public List<String> getSelectedApplications() {
        return selectedApplications;
    }

    public boolean wasCancelled() {
        return cancelled;
    }

    public void setApplicationsNames(List<String> applicationsNames) {
        this.applicationsNames = applicationsNames;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setCheckboxes() {
        boolean areAllCheckboxesSelected=areAllCheckboxesSelected();
        for (JFXCheckBox box : checkBoxes) {
            box.setSelected(!areAllCheckboxesSelected);
        }
    }

    private boolean areAllCheckboxesSelected() {
        for (JFXCheckBox box : checkBoxes) {
            if (!box.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private void initializeCheckBoxes(List<String> applicationNames) {
        for (String app : applicationNames) {
            JFXCheckBox option = new JFXCheckBox(app);
            option.setCheckedColor(Color.BLACK);
            option.setSelected(true);
            checkBoxes.add(option);
        }
    }

    private void setSelectedApplications(List<JFXCheckBox> checkBoxes) {
        for (JFXCheckBox box : checkBoxes)
            if (box.isSelected()) {
                selectedApplications.add(box.getText());
            }
    }

    public void reflesh() {
        borderPane.setStyle("text-color: #" + MotivesPresenter.textColor.toString().substring(2, 8) + ";" +
                "controller-color: #" + MotivesPresenter.controllerColor.toString().substring(2, 8) + ";" +
                "background-color: #" + MotivesPresenter.backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-color: #" + MotivesPresenter.ripplerColor.toString().substring(2, 8) + ";"+
                "second-color: #" + MotivesPresenter.secondColor.toString().substring(2, 8) + ";"+
                "background-text-color: #" + MotivesPresenter.backgroundTextColor.toString().substring(2, 8) + ";");
    }
}
