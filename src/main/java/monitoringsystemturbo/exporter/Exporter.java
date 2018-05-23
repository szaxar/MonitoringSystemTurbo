package monitoringsystemturbo.exporter;

import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Exporter {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String hourSummary = "h";
    private static final String minuteSummary = "min";
    private static final String secondSummary = "s";

    private String filename;
    private ArrayList<ComputerStatistics> computerStatisticsList = new ArrayList<>();
    private HashMap<String, List<Timeline>> applicationTimelinesMap = new HashMap<>();

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

    public void exportGeneralInfo() throws IOException{
        CsvBuilder csvBuilder = new CsvBuilder();
        csvBuilder.writeRow("", "RunningTime", "ActiveTime");
        int runningTime = 0, activeTime;
        for (ComputerStatistics computerStatistics : computerStatisticsList) {
            runningTime += computerStatistics.getRunningTimeInSec();
        }
        csvBuilder.writeRow("Computer", getDurationFormat(runningTime));
        for (Map.Entry<String, List<Timeline>> entry : applicationTimelinesMap.entrySet()) {
            runningTime = activeTime = 0;
            for (Timeline timeline : entry.getValue()) {
                runningTime += timeline.getRunningTimeInSec();
                activeTime += timeline.getActiveTimeInSec();
            }
            csvBuilder.writeRow(entry.getKey(), getDurationFormat(runningTime), getDurationFormat(activeTime));
        }
        saveFile(getGeneralFilename(), csvBuilder.build());
    }


    public void exportDetailInfo(Date... dates) throws IOException {
        String datetimeStart;
        String datetimeEnd;
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
        for (Map.Entry<String, List<Timeline>> entry : applicationTimelinesMap.entrySet()) {
            csvBuilder.nextRow();
            csvBuilder.writeRow(entry.getKey());
            csvBuilder.writeRow("DatetimeStart", "DatetimeEnd", "RunningTime", "ActiveTime");
            for (Timeline timeline : entry.getValue()) {
                datetimeStart = datetimeFormat.format(timeline.getDatetimeStart());
                datetimeEnd = datetimeFormat.format(timeline.getDatetimeEnd());
                if (dates.length > 0) {
                    String fromDate = datetimeFormat.format(dates[0]);
                    String toDate = datetimeFormat.format(dates[1]);
                    if (areDatesInInterval(fromDate, toDate, datetimeStart, datetimeEnd))
                        csvBuilder.writeRow(
                                datetimeStart,
                                datetimeEnd,
                                getDurationFormat(timeline.getRunningTimeInSec()),
                                getDurationFormat(timeline.getActiveTimeInSec())
                        );
                } else {
                    csvBuilder.writeRow(
                            datetimeStart,
                            datetimeEnd,
                            getDurationFormat(timeline.getRunningTimeInSec()),
                            getDurationFormat(timeline.getActiveTimeInSec())
                    );
                }

            }
        }
        saveFile(getDetailFilename(), csvBuilder.build());
    }

    private boolean areDatesInInterval(String fromDate, String toDate, String startDate, String endDate) {
        boolean startDateInInterval = (startDate.compareTo(fromDate) >= 0 && startDate.compareTo(toDate) <= 0);
        boolean endDateInInterval = (endDate.compareTo(fromDate) >= 0 && endDate.compareTo(toDate) <= 0);
        return (startDateInInterval && endDateInInterval);
    }

    private String getDurationFormat(int duration) {
        if (duration == 0) {
            return "0" + secondSummary;
        }
        int sec = duration % 60;
        duration /= 60;
        int min = duration % 60;
        duration /= 60;
        int h = duration;
        StringBuilder durationFormat = new StringBuilder();
        if (h != 0) {
            durationFormat.append(h).append(hourSummary);
        }
        if (min != 0) {
            if (durationFormat.length() > 0) {
                durationFormat.append(" ");
            }
            durationFormat.append(min).append(minuteSummary);
        }
        if (sec != 0) {
            if (durationFormat.length() > 0) {
                durationFormat.append(" ");
            }
            durationFormat.append(sec).append(secondSummary);
        }
        return durationFormat.toString();
    }

    private String getGeneralFilename() {
        return this.filename + "-general.csv";
    }

    private String getDetailFilename() {
        return this.filename + "-detail.csv";
    }

    private void saveFile(String filename, String content) throws IOException{
            Writer writer = new BufferedWriter(new FileWriter(filename, false));
            writer.write(content);
            writer.close();
    }

}
