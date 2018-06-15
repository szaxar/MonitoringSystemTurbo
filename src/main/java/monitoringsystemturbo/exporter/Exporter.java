package monitoringsystemturbo.exporter;

import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.utils.DateFormats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static monitoringsystemturbo.utils.DateFormats.getDurationFormat;

public class Exporter {

    private final String filename;
    private final ArrayList<ComputerStatistics> computerStatisticsList = new ArrayList<>();
    private final HashMap<String, List<Timeline>> applicationTimelinesMap = new HashMap<>();

    public Exporter(String filename) {
        this.filename = filename;
    }

    public void addComputerStatistics(ComputerStatistics computerStatistics) {
        computerStatisticsList.add(computerStatistics);
    }

    public void addApplicationStatistics(String applicationName, Timeline timeline) {
        if (applicationTimelinesMap.containsKey(applicationName)) {
            applicationTimelinesMap.get(applicationName).add(timeline);
            return;
        }
        ArrayList<Timeline> timelines = new ArrayList<>();
        timelines.add(timeline);
        applicationTimelinesMap.put(applicationName, timelines);
    }

    void addApplicationsStatistics(Map<String, List<Timeline>> appStatistics) {
        for (String appName : appStatistics.keySet()) {
            applicationTimelinesMap.put(appName, appStatistics.get(appName));
        }
    }

    public void exportGeneralInfo() throws IOException {
        GeneralCsv generalCsv = new GeneralCsv.Builder()
                .setComputerStatisticsList(computerStatisticsList)
                .setApplicationTimelinesMap(applicationTimelinesMap)
                .build();
        saveFile(getGeneralFilename(), generalCsv.build());
    }

    public void exportGeneralInfo(Date fromDate, Date toDate) throws IOException {
        GeneralCsv generalCsv = new GeneralCsv.Builder()
                .setComputerStatisticsList(computerStatisticsList)
                .setApplicationTimelinesMap(applicationTimelinesMap)
                .setFromDate(fromDate)
                .setToDate(toDate)
                .build();
        saveFile(getGeneralFilename(), generalCsv.build());
    }

    public void exportDetailInfo() throws IOException {
        DetailCsv detailCsv = new DetailCsv.Builder()
                .setComputerStatisticsList(computerStatisticsList)
                .setApplicationTimelinesMap(applicationTimelinesMap)
                .build();
        saveFile(getDetailFilename(), detailCsv.build());
    }

    public void exportDetailInfo(Date fromDate, Date toDate) throws IOException {
        DetailCsv detailCsv = new DetailCsv.Builder()
                .setComputerStatisticsList(computerStatisticsList)
                .setApplicationTimelinesMap(applicationTimelinesMap)
                .setFromDate(fromDate)
                .setToDate(toDate)
                .build();
        saveFile(getDetailFilename(), detailCsv.build());
    }

    private String getGeneralFilename() {
        return this.filename + "-general.csv";
    }

    private String getDetailFilename() {
        return this.filename + "-detail.csv";
    }

    private void saveFile(String filename, String content) throws IOException {
        Writer writer = new BufferedWriter(new FileWriter(filename, false));
        writer.write(content);
        writer.close();
    }

}