package monitoringsystemturbo.presenter;

import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import monitoringsystemturbo.model.app.Application;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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
            public void updateItem(Application application, boolean empty) {
                super.updateItem(application, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    File file = new File(application.getFullPath());
                    ShellFolder sf=null;
                    try {
                        sf = ShellFolder.getShellFolder(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Icon icon = new ImageIcon(sf.getIcon(true));

                    BufferedImage bufferedImage = imageToBufferedImage(iconToImage(icon));
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                    imageView.setImage(image);
                    setText(application.getName());
                    setGraphic(imageView);
                }
            }
        });
    }

}

