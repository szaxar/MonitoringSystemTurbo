package monitoringsystemturbo.presenter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import monitoringsystemturbo.model.app.Application;

import java.util.List;

import static monitoringsystemturbo.utils.IconConverter.iconToFxImage;

public class MainPresenter {

    private List<Application> loadedApplications;
    @FXML
    private ListView<Application> applicationList;

    @FXML
    public void initialize(List<Application> loadedApplications) {
        this.loadedApplications = loadedApplications;
        applicationList.setItems(FXCollections.observableList(loadedApplications));
        setCellFactory();
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

    private void setCellFactory() {
        applicationList.setCellFactory(param -> new ListCell<Application>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Application application, boolean empty) {
                super.updateItem(application, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Image image = iconToFxImage(application.findIcon());
                    imageView.setImage(image);
                    setText(application.getName());
                    setGraphic(imageView);
                }
            }
        });
    }

}

