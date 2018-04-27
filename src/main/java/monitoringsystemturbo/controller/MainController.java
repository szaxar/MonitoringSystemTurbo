package monitoringsystemturbo.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import monitoringsystemturbo.config.ConfigManager;
import monitoringsystemturbo.model.app.Application;
import monitoringsystemturbo.presenter.AddApplicationPresenter;
import monitoringsystemturbo.presenter.MainPresenter;
import java.io.IOException;
import java.util.List;

public class MainController {

    private Stage primaryStage;
    private List<Application> loadedApplications;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/main.fxml"));
        Parent rootLayout = loader.load();

        openConfig();
        MainPresenter mainPresenter=loader.getController();
        mainPresenter.setMainController(this);
        mainPresenter.initialize(loadedApplications);

        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();
    }



    private void openConfig(){
        try {
            loadedApplications = ConfigManager.load();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Error occurred while reading from config file");
        }
        if(loadedApplications == null){
            System.out.println("Error occurred while reading from config file");
            System.exit(1);
        }
    }

    public Application showAddView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/addView.fxml"));
        Parent rootLayout = loader.load();



        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add application");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        AddApplicationPresenter addApplicationPresenter=loader.getController();
        addApplicationPresenter.setPrimaryStage(dialogStage);

        Scene scene=new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        Application application= addApplicationPresenter.onAdd();
        Boolean isCancel=addApplicationPresenter.getCancel();
        if(isCancel.equals(false)) return application;
        else return null;
    }

}
