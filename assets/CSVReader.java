package assets;

import java.util.ArrayList;

public class CSVReader {

    private ArrayList<String> columnHeaders, rowIndices;
    private String[][] contents;

    public CSVReader(String csvUrl, char delimiter) {
        this.columnHeaders = new ArrayList<>();
        this.rowIndices = new ArrayList<>();
        String str = Assets.read(csvUrl, true);
        String[] rows = str.split("\n");
        String[] headers = rows[0].split(delimiter+"+");
        this.contents = new String[rows.length][];
        for (String header: headers) columnHeaders.add(header);
        for (int r = 0; r < rows.length; r++) {
            String[] cells = rows[r].split(delimiter+"+");
            rowIndices.add(cells[0]);
            contents[r] = cells;
        }
    }

    public int getRowCount() { return rowIndices.size(); }
    public int getColumnCount() { return columnHeaders.size(); }

    public String get(String column, int row) { return contents[row][columnHeaders.indexOf(column)]; }
    public String get(int column, int row) {
        return contents[row][column];
    }

    public String get(String header, String index) {
        int headerIndex = columnHeaders.indexOf(header);
        int rowIndex = rowIndices.indexOf(index);
        return get(headerIndex, rowIndex);
    }

    public double getDouble(String header, String index) {
        return Double.parseDouble(get(header, index));
    }

    public int getInteger(String header, String index) {
        return Integer.parseInt(get(header, index));
    }

    public boolean getBoolean(String header, String index) {
        return Boolean.parseBoolean(get(header, index));
    }

    public String[] getList(String header, String index) {
        return get(header, index).split(",\\s*");
    }

}