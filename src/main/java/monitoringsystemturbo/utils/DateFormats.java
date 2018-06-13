package monitoringsystemturbo.utils;

import java.text.SimpleDateFormat;

public class DateFormats {

    private static final String hourSummary = "h";
    private static final String minuteSummary = "min";
    private static final String secondSummary = "s";

    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static String getDurationFormat(int duration) {
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

}
