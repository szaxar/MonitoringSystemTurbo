import java.text.SimpleDateFormat;
import java.util.Date;

public class ComputerStatistics extends Thread {

    private boolean isRunning = true;
    private Date systemStartTime;
    private Date systemCloseTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public ComputerStatistics(Date systemStartTime) {
        this.systemStartTime = systemStartTime;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {}
            this.systemCloseTime = new Date();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isRunning = false;
    }

    @Override
    public String toString() {
        return "ComputerStatistics{" +
                "systemStartTime=" + sdf.format(systemStartTime.getTime()) +
                ", systemCloseTime=" + sdf.format(systemCloseTime.getTime()) +
                '}';
    }
}
