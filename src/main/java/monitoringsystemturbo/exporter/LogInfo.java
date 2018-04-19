package monitoringsystemturbo.exporter;

public class LogInfo {
    private String name;
    private String logDate;
    private String startTime;
    private String endTime;
    private String activeHours;
    private String passiveHours;

    public LogInfo(String name, String logDate, String startTime, String endTime, String activeHours, String passiveHours) {
        this.name = name;
        this.logDate = logDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activeHours = activeHours;
        this.passiveHours = passiveHours;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActiveHours() {
        return activeHours;
    }

    public void setActiveHours(String activeHours) {
        this.activeHours = activeHours;
    }

    public String getPassiveHours() {
        return passiveHours;
    }

    public void setPassiveHours(String passiveHours) {
        this.passiveHours = passiveHours;
    }

    @Override
    public String toString() {
        return this.name + " " + this.logDate + " " + this.startTime + " "
                + this.endTime + " " + this.activeHours + " " + this.passiveHours;
    }
}
