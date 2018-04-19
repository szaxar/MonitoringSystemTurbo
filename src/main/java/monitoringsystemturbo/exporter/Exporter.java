package monitoringsystemturbo.exporter;
import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exporter {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String filename;
    private ArrayList<ComputerStatistics> computerStatisticsList = new ArrayList<>();
    private HashMap<String, ArrayList<Timeline>> applicationTimelinesMap = new HashMap<>();

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

    public void exportGeneralInfo() {
        CsvBuilder csvBuilder = new CsvBuilder();
        csvBuilder.writeRow("", "RunningTime", "ActiveTime");
        int runningTime = 0, activeTime;
        for (ComputerStatistics computerStatistics : computerStatisticsList) {
            runningTime += computerStatistics.getRunningTimeInSec();
        }
        csvBuilder.writeRow("Computer", getDurationFormat(runningTime));
        for (Map.Entry<String, ArrayList<Timeline>> entry : applicationTimelinesMap.entrySet()) {
            runningTime = activeTime = 0;
            for (Timeline timeline : entry.getValue()) {
                runningTime += timeline.getRunningTimeInSec();
                activeTime += timeline.getActiveTimeInSec();
            }
            csvBuilder.writeRow(entry.getKey(), getDurationFormat(runningTime), getDurationFormat(activeTime));
        }
        saveFile(getGeneralFilename(), csvBuilder.build());
    }

    public void exportDetailInfo() {
        CsvBuilder csvBuilder = new CsvBuilder();
        csvBuilder.writeRow("Computer");
        csvBuilder.writeRow("SystemStartDatetime", "SystemCloseDatetime", "RunningTime");
        for (ComputerStatistics computerStatistics : computerStatisticsList) {
            csvBuilder.writeRow(
                    datetimeFormat.format(computerStatistics.getSystemStartTime()),
                    datetimeFormat.format(computerStatistics.getSystemCloseTime()),
                    getDurationFormat(computerStatistics.getRunningTimeInSec())
            );
        }
        for (Map.Entry<String, ArrayList<Timeline>> entry : applicationTimelinesMap.entrySet()) {
            csvBuilder.nextRow();
            csvBuilder.writeRow(entry.getKey());
            csvBuilder.writeRow("DatetimeStart", "DatetimeEnd", "RunningTime", "ActiveTime");
            for (Timeline timeline : entry.getValue()) {
                csvBuilder.writeRow(
                        datetimeFormat.format(timeline.getDatetimeStart()),
                        datetimeFormat.format(timeline.getDatetimeEnd()),
                        getDurationFormat(timeline.getRunningTimeInSec()),
                        getDurationFormat(timeline.getActiveTimeInSec())
                );
            }
        }
        saveFile(getDetailFilename(), csvBuilder.build());
    }

    private String getDurationFormat(int duration) {
        if (duration == 0) {
            return "0s";
        }
        int sec = duration % 60;
        duration /= 60;
        int min = duration % 60;
        duration /= 60;
        int h = duration;
        StringBuilder durationFormat = new StringBuilder();
        if (h != 0) {
            durationFormat.append(h).append("h");
        }
        if (min != 0) {
            if (durationFormat.length() > 0) {
                durationFormat.append(" ");
            }
            durationFormat.append(min).append("min");
        }
        if (sec != 0) {
            if (durationFormat.length() > 0) {
                durationFormat.append(" ");
            }
            durationFormat.append(sec).append("s");
        }
        return durationFormat.toString();
    }

    private String getGeneralFilename() {
        return this.filename + "-general.csv";
    }

    private String getDetailFilename() {
        return this.filename + "-detail.csv";
    }

    private void saveFile(String filename, String content) {
        try {
            Writer writer = new BufferedWriter(new FileWriter(filename, false));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
