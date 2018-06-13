package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import monitoringsystemturbo.model.ActivityMonitor;

public class OptionsPresenter {
    private Stage primaryStage;
    private ActivityMonitor activityMonitor;

    @FXML
    private ChoiceBox timeDurationChoiceBox;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setActivityMonitor(ActivityMonitor activityMonitor) {
        this.activityMonitor = activityMonitor;
    }

    public void onConfirm() {
        String option = timeDurationChoiceBox.getValue().toString().split(" ")[0];
        if (option.equals("Off"))
            activityMonitor.setExtendedMonitoring(false);
        else
            activityMonitor.setStopTime(Integer.parseInt(option));
        primaryStage.close();
    }

    public void onCancel() {
        primaryStage.close();
    }
}
