package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfirmExportPresenter {

    @FXML
    private VBox checkBoxesContainer;

    private Stage primaryStage;
    private List<String> applicationsNames;

    private List<CheckBox> checkBoxes = new ArrayList<>();
    private List<String> applicationsToExport = new ArrayList<>();
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
        for (CheckBox checkBox : checkBoxes) {
            checkBoxesContainer.getChildren().add(checkBox);
        }
    }

    public List<String> getApplicationsToExport() {
        return applicationsToExport;
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
        boolean areAllCheckboxesSelected = areAllCheckboxesSelected();
        for (CheckBox box : checkBoxes) {
            box.setSelected(!areAllCheckboxesSelected);
        }
    }

    private boolean areAllCheckboxesSelected() {
        for (CheckBox box : checkBoxes) {
            if (!box.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private void initializeCheckBoxes(List<String> applicationNames) {
        for (String app : applicationNames) {
            CheckBox option = new CheckBox(app);
            option.setSelected(true);
            checkBoxes.add(option);
        }
    }

    private void setSelectedApplications(List<CheckBox> checkBoxes) {
        for (CheckBox box : checkBoxes)
            if (box.isSelected()) {
                applicationsToExport.add(box.getText());
            }
    }
}
