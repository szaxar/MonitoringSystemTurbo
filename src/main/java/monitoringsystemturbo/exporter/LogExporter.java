package monitoringsystemturbo.exporter;
import java.io.*;

public class LogExporter {
    private String filePath;
    private static final String SEPARATOR = ",";

    public LogExporter(String filePath) {
        this.filePath = filePath;
    }

    public void saveLog(LogInfo log) {

        String[] logArray = log.toString().split(" ");
        String toSave = "";
        for (int i = 0; i < logArray.length - 1; i++)
            toSave += logArray[i] + SEPARATOR;
        toSave += logArray[logArray.length - 1];
            try {
                Writer writer = new BufferedWriter(new FileWriter(this.filePath, true));
                File file = new File(this.filePath);
                if(file.length()==0) {
                    writer.write("Process, Date, StartTime, EndTime, RunningSeconds, ActiveSeconds\n");
                }

                writer.write(toSave + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
