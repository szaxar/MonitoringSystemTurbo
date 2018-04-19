package monitoringsystemturbo.exporter;

public class CsvBuilder {

    private static final String SEPARATOR = ",";
    private StringBuilder csvContent = new StringBuilder();

    public void writeRow(String... cells) {
        writeCells(cells);
        nextRow();
    }

    public void writeCells(String... cells) {
        for (String cell : cells) {
            csvContent.append(cell).append(SEPARATOR);
        }
    }

    public void nextRow() {
        csvContent.append('\n');
    }

    public String build() {
        return csvContent.toString();
    }

}
