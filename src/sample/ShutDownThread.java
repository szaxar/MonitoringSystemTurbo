package sample;

import java.util.Date;

public class ShutDownThread extends Thread {
    private ComputerStatistics computerStatistics;

    public ShutDownThread(ComputerStatistics computerStatistics) {
        this.computerStatistics = computerStatistics;
    }

    public void run() {
        Date systemCloseTime = new Date();
        computerStatistics.setSystemCloseTime(systemCloseTime);
        System.out.println(computerStatistics.toString());
    }
}
