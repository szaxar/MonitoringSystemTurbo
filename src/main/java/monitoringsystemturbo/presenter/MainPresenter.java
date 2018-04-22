package monitoringsystemturbo.presenter;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import monitoringsystemturbo.model.app.Application;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static monitoringsystemturbo.utils.IconConverter.iconToImage;
import static monitoringsystemturbo.utils.IconConverter.imageToBufferedImage;


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


    private void setCellFactory(){
        applicationList.setCellFactory(param -> new ListCell<Application>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Application name, boolean empty) {
                super.updateItem(name, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Icon icon = FileSystemView.getFileSystemView()
                            .getSystemIcon(new File(name.getFullPath()));

                    BufferedImage bufferedImage = imageToBufferedImage(iconToImage(icon));
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                    imageView.setImage(image);
                    setText(name.getName());
                    setGraphic(imageView);
                }
            }
        });
    }

}

