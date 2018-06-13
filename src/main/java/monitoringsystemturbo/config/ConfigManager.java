package monitoringsystemturbo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import monitoringsystemturbo.controller.AlertController;
import monitoringsystemturbo.model.app.Application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigManager {
    private static ObjectMapper mapper = new ObjectMapper();

    private ConfigManager() {
    }

    public static void save(List<Application> list) throws IOException {
        mapper.writeValue(new File("config.json"), list);
    }

    public static List<Application> load() throws IOException {
        File file = new File("config.json");
        if (file.exists()) {
            List<Application> list = mapper.readValue(file, new TypeReference<List<Application>>() {
            });
            List<Application> removedApplications = checkApplicationList(list);
            if (!removedApplications.isEmpty()) {
                StringBuilder removedAppNames = new StringBuilder();
                for (Application application : removedApplications) {
                    removedAppNames.append("- ").append(application.getName()).append(" (")
                            .append(application.getFullPath()).append(")").append("\n");
                }
                AlertController.showAlert("Following applications were not found and will be removed from list:\n"
                        + removedAppNames, Alert.AlertType.WARNING);
                save(list);
            }
            return list;
        }
        return new ArrayList<>();
    }

    private static List<Application> checkApplicationList(List<Application> loadedApplications) {
        Iterator<Application> iter = loadedApplications.iterator();
        List<Application> notFound = new ArrayList<>();

        while (iter.hasNext()) {
            Application application = iter.next();
            String fullPath = application.getFullPath();
            if (!fullPath.equals("")) {
                File file = new File(fullPath);
                if (!file.exists()) {
                    notFound.add(application);
                    iter.remove();
                }
            }
        }

        return notFound;
    }

    public static void createFileIfNeeded(Application application) throws IOException {
        File file = new File(application.getName() + ".json");
        file.createNewFile();
    }
}
