package monitoringsystemturbo.exporter;

import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainExporter {
    private static final String FILENAME = "report";

    public void export(TrackingService trackingService, List<String> applicationsToExport) throws IOException {
        Exporter exporter = new Exporter(FILENAME);
        addHistoricalComputerStatistics(exporter);
        addHistoricalAppStatistics(exporter, applicationsToExport);
        addCurrentStatistics(exporter, trackingService, applicationsToExport);

        exporter.exportGeneralInfo();
        exporter.exportDetailInfo();
    }

    public void export(TrackingService trackingService, List<String> applicationsToExport, LocalDateTime fromTime, LocalDateTime toTime) throws IOException {
        Exporter exporter = new Exporter(FILENAME);
        addHistoricalComputerStatistics(exporter);
        addHistoricalAppStatistics(exporter, applicationsToExport);
        addCurrentStatistics(exporter, trackingService, applicationsToExport);

        exporter.exportGeneralInfo(Date.from(fromTime.atZone(ZoneId.systemDefault()).toInstant()), Date.from(toTime.atZone(ZoneId.systemDefault()).toInstant()));
        exporter.exportDetailInfo(Date.from(fromTime.atZone(ZoneId.systemDefault()).toInstant()), Date.from(toTime.atZone(ZoneId.systemDefault()).toInstant()));
    }


    private void addHistoricalComputerStatistics(Exporter exporter) {
        for (ComputerStatistics statistics : StatisticsManager.loadComputerStats()) {
            exporter.addComputerStatistics(statistics);
        }
    }

    private void addHistoricalAppStatistics(Exporter exporter, List<String> applicationsToExport) throws IOException {
        Map<String, List<Timeline>> appTimelines = new HashMap<>();
        for (String appName : applicationsToExport) {
            appTimelines.put(appName, StatisticsManager.load(appName));
        }
        exporter.addApplicationsStatistics(appTimelines);
    }

    private void addCurrentStatistics(Exporter exporter, TrackingService trackingService, List<String> applicationsToExport) {
        exporter.addComputerStatistics(trackingService.getComputerStatistics());
        for (String name : applicationsToExport) {
            Timeline statistics = trackingService.getStatisticsForApp(name);
            exporter.addApplicationStatistics(name, statistics);
        }
    }
}
