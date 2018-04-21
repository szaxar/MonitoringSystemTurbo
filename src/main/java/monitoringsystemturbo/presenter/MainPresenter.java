package monitoringsystemturbo.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import monitoringsystemturbo.model.app.Application;

import java.util.List;


public class MainPresenter {


    private List<Application> loadedApplications;
    @FXML
    private ListView<Application> applicationList;


    @FXML
    public void initialize(List<Application> loadedApplications) {
        this.loadedApplications = loadedApplications;
        for (Application application : loadedApplications) {
            applicationList.getItems().add(application);
        }

        applicationList.setCellFactory(param -> new ListCell<Application>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Application name, boolean empty) {
                super.updateItem(name, empty);
                //imageView=new ImageView( name.getIcon());
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name.getName());
                    //setGraphic(imageView);
                }
            }
        });

    }

    @FXML
    public void onAddApplication() {
    }

    @FXML
    public void onRemoveApplication() {
    }

    @FXML
    public void onExport() {
    }


}
