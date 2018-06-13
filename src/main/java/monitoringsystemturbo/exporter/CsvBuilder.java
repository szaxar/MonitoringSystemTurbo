package monitoringsystemturbo.exporter;

public class CsvBuilder {

    private static final String SEPARATOR = ";";
    private StringBuilder csvContent = new StringBuilder();

    public CsvBuilder writeRow(String... cells) {
        writeCells(cells);
        nextRow();
        return this;
    }

    public CsvBuilder writeCells(String... cells) {
        for (String cell : cells) {
            csvContent.append(cell).append(SEPARATOR);
        }
        return this;
    }

    public CsvBuilder nextRow() {
        csvContent.append('\n');
        return this;
    }

    public String build() {
        return csvContent.toString();
    }

}
