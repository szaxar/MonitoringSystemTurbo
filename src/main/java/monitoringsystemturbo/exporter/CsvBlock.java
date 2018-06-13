package monitoringsystemturbo.exporter;

import java.util.ArrayList;
import java.util.List;

public class CsvBlock {

    private final List<List<String>> block = new ArrayList<>();
    private int cursorX = 0;
    private int cursorY = 0;

    public CsvBlock writeRow(String... cells) {
        return writeCells(cells).nextRow();
    }

    public CsvBlock writeCells(String... cells) {
        for (String cell : cells) {
            writeCell(cell);
        }
        return this;
    }

    private CsvBlock writeCell(String cell) {
        if (!cellExists()) {
            createCell();
        }
        block.get(cursorY).set(cursorX, cell);
        cursorX++;
        return this;
    }

    public CsvBlock nextRow() {
        cursorX = 0;
        cursorY++;
        return this;
    }

    private boolean cellExists() {
        return cursorY < block.size() && cursorX < block.get(cursorY).size();
    }

    private void createCell() {
        while (cursorY >= block.size()) {
            block.add(new ArrayList<>());
        }
        while (cursorX >= block.get(cursorY).size()) {
            block.get(cursorY).add("");
        }
    }

    int getWidth() {
        int width = 0;
        for (List<String> row : block) {
            if (row.size() > width) {
                width = row.size();
            }
        }
        return width;
    }

    int getHeight() {
        return block.size();
    }

    List<String> getRow(int index) {
        return block.get(index);
    }

    public CsvBlock appendRight(CsvBlock csvBlock) {
        return appendRight(csvBlock, 0);
    }

    public CsvBlock appendRight(CsvBlock csvBlock, int space) {
        int height = Math.max(this.getHeight(), csvBlock.getHeight());
        while (block.size() < height) {
            block.add(new ArrayList<>());
        }
        int width = getWidth();
        for (List<String> row : block) {
            while (row.size() < width) {
                row.add("");
            }
        }
        for (List<String> row : block) {
            for (int i = 0; i < space; i++) {
                row.add("");
            }
        }
        for (int y = 0; y < csvBlock.getHeight(); y++) {
            this.getRow(y).addAll(csvBlock.getRow(y));
        }
        return this;
    }

    public String build() {
        CsvBuilder csvBuilder = new CsvBuilder();
        for (List<String> row : block) {
            csvBuilder.writeRow(row.toArray(new String[row.size()]));
        }
        return csvBuilder.build();
    }

}
