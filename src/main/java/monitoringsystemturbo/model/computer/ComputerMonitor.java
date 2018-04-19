package monitoringsystemturbo.model.computer;

import java.util.Date;

public class ComputerMonitor extends Thread {
    private boolean isRunning = true;
    private ComputerStatistics computerStatistics;

    public ComputerMonitor(ComputerStatistics computerStatistics) {
        this.computerStatistics = computerStatistics;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            computerStatistics.setSystemCloseTime(new Date());
        }
    }

    @Override
    public void interrupt() {
        isRunning = false;
        super.interrupt();
    }
}
