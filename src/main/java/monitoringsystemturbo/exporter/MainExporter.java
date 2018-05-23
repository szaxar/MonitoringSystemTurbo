package monitoringsystemturbo.exporter;

import monitoringsystemturbo.history.StatisticsManager;
import monitoringsystemturbo.model.TrackingService;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainExporter {
    private static final String REPORT = "report";

    public void export(TrackingService trackingService, List<String> applicationsToExport) throws IOException{
        Exporter exporter = new Exporter(REPORT);

        addHistoricalComputerStatistics(exporter);
        addHistoricalAppStatistics(exporter, applicationsToExport);
        addCurrentStatistics(exporter, trackingService, applicationsToExport);

        exporter.exportGeneralInfo();
        exporter.exportDetailInfo();
    }

    public void export(TrackingService trackingService, List<String> applicationsToExport, LocalDate fromDate , LocalDate toDate) throws IOException{
        Exporter exporter = new Exporter(REPORT);

        addHistoricalComputerStatistics(exporter);
        addHistoricalAppStatistics(exporter, applicationsToExport);
        addCurrentStatistics(exporter, trackingService, applicationsToExport);

        exporter.exportGeneralInfo();
        exporter.exportDetailInfo(Date.from(fromDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(toDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
    }


    private void addHistoricalComputerStatistics(Exporter exporter) throws IOException {
        for (ComputerStatistics statistics : StatisticsManager.loadComputerStats()) {
            exporter.addComputerStatistics(statistics);
        }
    }

    private void addHistoricalAppStatistics(Exporter exporter,  List<String> applicationsToExport) throws IOException {
        Map<String, List<Timeline>> appTimelines = new HashMap<>();
        for (String appName : applicationsToExport) {
            appTimelines.put(appName, StatisticsManager.load(appName));
        }
        exporter.addApplicationsStatistics(appTimelines);
    }

    private void addCurrentStatistics(Exporter exporter, TrackingService trackingService,  List<String> applicationsToExport) {
        exporter.addComputerStatistics(trackingService.getComputerStatistics());
        for (String name : applicationsToExport) {
            Timeline statistics = trackingService.getStatisticsForApp(name);
            exporter.addApplicationStatistics(name, statistics);
        }
    }
}
