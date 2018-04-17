import java.io.*;

public class LogsExporter {
    private String filePath;
    private static final String SEPARATOR = ",";

    public LogsExporter(String filePath) {
        this.filePath = filePath;
    }

    public void saveLog(LogInfo log) {
        System.out.print(this.filePath);
        String[] logArray = log.toString().split(" ");
        String toSave = "";
        for (int i = 0; i < logArray.length - 1; i++)
            toSave += logArray[i] + SEPARATOR;
        toSave += logArray[logArray.length - 1];
            try {
                Writer writer = new BufferedWriter(new FileWriter(this.filePath, true));
                writer.write(toSave + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
