package monitoringsystemturbo.exporter;

import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainExporter {
    private static final String REPORT = "report";

    public void export(TrackingService trackingService) throws IOException{
        Exporter exporter = new Exporter(REPORT);

        addHistoricalComputerStatistics(exporter);
        addHistoricalAppStatistics(exporter, trackingService);
        addCurrentStatistics(exporter, trackingService);

        exporter.exportGeneralInfo();
        exporter.exportDetailInfo();
    }

    private void addHistoricalComputerStatistics(Exporter exporter) throws IOException {
        for (ComputerStatistics statistics : StatisticsManager.loadComputerStats()) {
            exporter.addComputerStatistics(statistics);
        }
    }

    private void addHistoricalAppStatistics(Exporter exporter, TrackingService trackingService) throws IOException {
        Map<String, List<Timeline>> appTimelines = new HashMap<>();
        for (String appName : trackingService.getApplicationsNames()) {
            appTimelines.put(appName, StatisticsManager.load(appName));
        }
        exporter.addApplicationsStatistics(appTimelines);
    }

    private void addCurrentStatistics(Exporter exporter, TrackingService trackingService) {
        exporter.addComputerStatistics(trackingService.getComputerStatistics());
        for (String name : trackingService.getApplicationsNames()) {
            Timeline statistics = trackingService.getStatisticsForApp(name);
            exporter.addApplicationStatistics(name, statistics);
        }
    }
}
