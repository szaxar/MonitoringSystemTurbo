package monitoringsystemturbo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import monitoringsystemturbo.controller.ErrorController;
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
            if(!checkApplicationList(list)) {
                ErrorController.showError("Some applications were not found and will be removed form list.");
                save(list);
            }
            return list;
        }
        return new ArrayList<>();
    }

    public static boolean checkApplicationList(List<Application> loadedApplications) {
        boolean allExists = true;
        Iterator<Application> iter = loadedApplications.iterator();

        while (iter.hasNext()) {
            Application application = iter.next();
            String fullPath = application.getFullPath();
            if (!fullPath.equals("")) {
                File file = new File(fullPath);
                if (!file.exists()) {
                    iter.remove();
                    allExists = false;
                }
            }
        }

        return allExists;
    }

    public static void createFileIfNeeded(Application application) throws IOException {
        File file = new File(application.getName() + ".json");
        file.createNewFile();
    }
}
