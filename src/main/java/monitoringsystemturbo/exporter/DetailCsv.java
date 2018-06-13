package monitoringsystemturbo.exporter;

import monitoringsystemturbo.model.computer.ComputerStatistics;
import monitoringsystemturbo.model.timeline.ActivePeriod;
import monitoringsystemturbo.model.timeline.Period;
import monitoringsystemturbo.model.timeline.RunningPeriod;
import monitoringsystemturbo.model.timeline.Timeline;
import monitoringsystemturbo.utils.DateFormats;

import java.util.*;

public class DetailCsv {

    private final static long dayInMs = 24 * 60 * 60 * 1000;
    private final static long hInMs = 60 * 60 * 1000;
    private final static long dateOffset = hInMs + TimeZone.getDefault().getRawOffset();

    public static class Builder {
        private DetailCsv detailCsv = new DetailCsv();

        public Builder setComputerStatisticsList(ArrayList<ComputerStatistics> computerStatisticsList) {
            this.detailCsv.computerStatisticsList = computerStatisticsList;
            return this;
        }

        public Builder setApplicationTimelinesMap(HashMap<String, List<Timeline>> applicationTimelinesMap) {
            this.detailCsv.applicationTimelinesMap = applicationTimelinesMap;
            return this;
        }

        public Builder setFromDate(Date fromDate) {
            this.detailCsv.fromDate = fromDate;
            return this;
        }

        public Builder setToDate(Date toDate) {
            this.detailCsv.toDate = toDate;
            return this;
        }

        public DetailCsv build() {
            return detailCsv;
        }
    }

    private ArrayList<ComputerStatistics> computerStatisticsList = new ArrayList<>();
    private HashMap<String, List<Timeline>> applicationTimelinesMap = new HashMap<>();
    private Date fromDate = null;
    private Date toDate = null;

    private DetailCsv() {}

    public String build() {
        CsvBlock csvBlock = getComputerCsvBlock();
        for (Map.Entry<String, List<Day>> entry : getApplicationDaysMap(applicationTimelinesMap).entrySet()) {
            CsvBlock applicationCsvBlock = getApplicationCsvBlock(entry.getKey(), entry.getValue());
            csvBlock.appendRight(applicationCsvBlock, 1);
        }
        return csvBlock.build();
    }

    private CsvBlock getComputerCsvBlock() {
        CsvBlock csvBlock = new CsvBlock();
        csvBlock.writeRow("Computer")
                .writeRow("Day", "SystemStartTime", "SystemCloseTime", "RunningTime");
        for (Day day : getComputerDays()) {
            csvBlock.writeRow(DateFormats.dateFormat.format(day.date),
                    DateFormats.timeFormat.format(day.startTime),
                    DateFormats.timeFormat.format(day.endTime),
                    DateFormats.getDurationFormat(day.runningTime));
        }
        return csvBlock;
    }

    private CsvBlock getApplicationCsvBlock(String application, List<Day> days) {
        CsvBlock csvBlock = new CsvBlock();
        csvBlock.writeRow(application)
                .writeRow("Day", "StartTime", "EndTime", "RunningTime", "ActiveTime");
        for (Day day : days) {
            csvBlock.writeRow(DateFormats.dateFormat.format(day.date),
                    DateFormats.timeFormat.format(day.startTime),
                    DateFormats.timeFormat.format(day.endTime),
                    DateFormats.getDurationFormat(day.runningTime),
                    DateFormats.getDurationFormat(day.activeTime));
        }
        return csvBlock;
    }

    private class Day implements Comparable<Day> {
        Date date;
        Date startTime;
        Date endTime;
        int runningTime = 0;
        int activeTime = 0;

        Day(Date date, Date startTime, Date endTime) {
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int compareTo(Day day) {
            return Long.compare(this.date.getTime(), day.date.getTime());
        }
    }

    private List<Day> getComputerDays() {
        List<Timeline> timelines = new ArrayList<>();
        for (ComputerStatistics computerStatistics : computerStatisticsList) {
            timelines.add(new Timeline(computerStatistics, false));
        }
        return getDaysByTimelines(timelines);
    }

    private HashMap<String, List<Day>> getApplicationDaysMap(HashMap<String, List<Timeline>> applicationTimelinesMap) {
        HashMap<String, List<Day>> applicationDaysMap = new HashMap<>();
        for (Map.Entry<String, List<Timeline>> applicationTimelines : applicationTimelinesMap.entrySet()) {
            List<Day> days = getDaysByTimelines(applicationTimelines.getValue());
            applicationDaysMap.put(applicationTimelines.getKey(), days);
        }
        return applicationDaysMap;
    }

    private List<Day> getDaysByTimelines(List<Timeline> timelines) {
        HashMap<Integer, Day> dayMap = new HashMap<>();
        for (Timeline timeline : timelines) {
            for (Period period : timeline.getPeriods()) {
                long datetimeStart = period.getDatetimeStart().getTime();
                if (fromDate != null && fromDate.getTime() > datetimeStart) {
                    datetimeStart = fromDate.getTime();
                }
                long datetimeEnd = period.getDatetimeEnd().getTime();
                if (toDate != null && toDate.getTime() < datetimeEnd) {
                    datetimeEnd = toDate.getTime();
                }
                for (int day = (int) ((datetimeStart + dateOffset) / dayInMs); day <= (datetimeEnd + dateOffset) / dayInMs; day++) {
                    long dayStart = day * dayInMs - dateOffset;
                    long dayEnd = (day + 1) * dayInMs - dateOffset - 1;
                    long datetimeStartInDay = Math.max(datetimeStart, dayStart);
                    long datetimeEndInDay = Math.min(datetimeEnd, dayEnd);
                    if (!dayMap.containsKey(day)) {
                        dayMap.put(day, new Day(new Date(dayStart), new Date(datetimeStartInDay), new Date(datetimeEndInDay)));
                    }
                    Day dayClass = dayMap.get(day);
                    if (period instanceof RunningPeriod) {
                        dayClass.runningTime += (datetimeEndInDay - datetimeStartInDay) / 1000;
                    } else if (period instanceof ActivePeriod) {
                        dayClass.activeTime += (datetimeEndInDay - datetimeStartInDay) / 1000;
                    }
                    if (datetimeStartInDay < dayClass.startTime.getTime()) {
                        dayClass.startTime = new Date(datetimeStartInDay);
                    }
                    if (datetimeEndInDay > dayClass.endTime.getTime()) {
                        dayClass.endTime = new Date(datetimeEndInDay);
                    }
                }
            }
        }
        List<Day> days = new ArrayList<>(dayMap.values());
        Collections.sort(days);
        return days;
    }

}
