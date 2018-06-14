package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import monitoringsystemturbo.model.ActionsMonitor;

public class OptionsPresenter {
    private Stage primaryStage;
    private ActionsMonitor actionsMonitor;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox timeDurationChoiceBox;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setActionsMonitor(ActionsMonitor actionsMonitor) {
        this.actionsMonitor = actionsMonitor;
    }

    public void start() {
        setSelection();
    }

    @FXML
    public void onConfirm() {
        String option = timeDurationChoiceBox.getValue().toString().split(" ")[0];
        if (option.equals("Off"))
            actionsMonitor.setExtendedMonitoring(false);
        else
            actionsMonitor.setStopTime(Integer.parseInt(option));
        primaryStage.close();
    }

    @FXML
    public void onCancel() {
        primaryStage.close();
    }

    private void setSelection() {
        timeDurationChoiceBox.getSelectionModel().selectFirst();
        if (actionsMonitor.isExtendedMonitoring()) {
            while (!timeDurationChoiceBox.getValue().toString()
                    .contains(Integer.toString(actionsMonitor.getStopTime()))) {
                timeDurationChoiceBox.getSelectionModel().selectNext();
            }
        }
    }

    public void reflesh() {
        anchorPane.setStyle("text-color: #" + MotivesPresenter.textColor.toString().substring(2, 8) + ";" +
                "controller-color: #" + MotivesPresenter.controllerColor.toString().substring(2, 8) + ";" +
                "background-color: #" + MotivesPresenter.backgroundColor.toString().substring(2, 8) + ";" +
                "rippler-color: #" + MotivesPresenter.ripplerColor.toString().substring(2, 8) + ";"+
                "second-color: #" + MotivesPresenter.secondColor.toString().substring(2, 8) + ";"+
                "background-text-color: #" + MotivesPresenter.backgroundTextColor.toString().substring(2, 8) + ";");
    }
}
