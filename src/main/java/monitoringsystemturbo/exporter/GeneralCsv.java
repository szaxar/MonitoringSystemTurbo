package monitoringsystemturbo.exporter;

import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.utils.DateFormats;

import java.util.*;

import static monitoringsystemturbo.utils.DateFormats.getDurationFormat;

public class GeneralCsv {

    public static class Builder {
        private GeneralCsv generalCsv = new GeneralCsv();

        public Builder setComputerStatisticsList(ArrayList<ComputerStatistics> computerStatisticsList) {
            this.generalCsv.computerStatisticsList = computerStatisticsList;
            return this;
        }

        public Builder setApplicationTimelinesMap(HashMap<String, List<Timeline>> applicationTimelinesMap) {
            this.generalCsv.applicationTimelinesMap = applicationTimelinesMap;
            return this;
        }

        public Builder setFromDate(Date fromDate) {
            this.generalCsv.fromDate = fromDate;
            return this;
        }

        public Builder setToDate(Date toDate) {
            this.generalCsv.toDate = toDate;
            return this;
        }

        public GeneralCsv build() {
            return generalCsv;
        }
    }

    private ArrayList<ComputerStatistics> computerStatisticsList = new ArrayList<>();
    private HashMap<String, List<Timeline>> applicationTimelinesMap = new HashMap<>();
    private Date fromDate = null;
    private Date toDate = null;

    private GeneralCsv() {}

    public String build() {
        CsvBuilder csvBuilder = new CsvBuilder();
        writeHeader(csvBuilder);
        writeComputerInfo(csvBuilder);
        writeApplicationsInfo(csvBuilder);
        return csvBuilder.build();
    }

    private void writeHeader(CsvBuilder csvBuilder) {
        csvBuilder.writeRow("", "RunningTime", "ActiveTime", "DatetimeStart", "DatetimeEnd");
    }

    private void writeComputerInfo(CsvBuilder csvBuilder) {
        Date datetimeStart = null, datetimeEnd = null;
        int runningTime = 0;
        for (ComputerStatistics computerStatistics : computerStatisticsList) {
            runningTime += computerStatistics.getRunningTimeInSec(fromDate, toDate);
            if (datetimeStart == null || computerStatistics.getSystemStartTime().before(datetimeStart)) {
                if (fromDate == null || computerStatistics.getSystemStartTime().getTime() >= fromDate.getTime()) {
                    datetimeStart = computerStatistics.getSystemStartTime();
                } else {
                    datetimeStart = fromDate;
                }
            }
            if (datetimeEnd == null || computerStatistics.getSystemCloseTime().after(datetimeEnd)) {
                if (toDate == null || computerStatistics.getSystemCloseTime().getTime() <= toDate.getTime()) {
                    datetimeEnd = computerStatistics.getSystemCloseTime();
                } else {
                    datetimeEnd = toDate;
                }
            }
        }
        csvBuilder.writeCells("Computer", getDurationFormat(runningTime), "")
                .writeCells(datetimeStart != null ? DateFormats.datetimeFormat.format(datetimeStart) : "")
                .writeCells(datetimeEnd != null ? DateFormats.datetimeFormat.format(datetimeEnd) : "")
                .nextRow();
    }

    private void writeApplicationsInfo(CsvBuilder csvBuilder) {
        Date datetimeStart, datetimeEnd;
        int runningTime, activeTime;
        for (Map.Entry<String, List<Timeline>> entry : applicationTimelinesMap.entrySet()) {
            runningTime = activeTime = 0;
            datetimeStart = datetimeEnd = null;
            for (Timeline timeline : entry.getValue()) {
                runningTime += timeline.getRunningTimeInSec(fromDate, toDate);
                activeTime += timeline.getActiveTimeInSec(fromDate, toDate);
                if (datetimeStart == null || timeline.getDatetimeStart().getTime() < datetimeStart.getTime()) {
                    if (fromDate == null || timeline.getDatetimeStart().getTime() >= fromDate.getTime()) {
                        datetimeStart = timeline.getDatetimeStart();
                    } else {
                        datetimeStart = fromDate;
                    }
                }
                if (datetimeEnd == null || timeline.getDatetimeEnd().getTime() > datetimeEnd.getTime()) {
                    if (toDate == null || timeline.getDatetimeEnd().getTime() <= toDate.getTime()) {
                        datetimeEnd = timeline.getDatetimeEnd();
                    } else {
                        datetimeEnd = toDate;
                    }
                }
            }
            csvBuilder.writeCells(entry.getKey(), getDurationFormat(runningTime), getDurationFormat(activeTime))
                    .writeCells(datetimeStart != null ? DateFormats.datetimeFormat.format(datetimeStart) : "")
                    .writeCells(datetimeEnd != null ? DateFormats.datetimeFormat.format(datetimeEnd) : "")
                    .nextRow();
        }
    }

}
