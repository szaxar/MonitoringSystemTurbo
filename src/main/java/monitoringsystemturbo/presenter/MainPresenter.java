package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import monitoringsystemturbo.model.app.Application;
import java.util.List;


public class MainPresenter {


    private List<Application> loadedApplications;
    @FXML
    private ListView<String> applicationList;

    @FXML
    public void initialize(List<Application> loadedApplications) {
        this.loadedApplications = loadedApplications;
        for (Application application : loadedApplications) {
            applicationList.getItems().add(application.getName());
        }
    }

    @FXML
    public void onAddApplication() {}

    @FXML
    public void onRemoveApplication() {}

    @FXML
    public void onExport() {}

}
