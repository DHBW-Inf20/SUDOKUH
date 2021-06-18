package view;

import javax.swing.*;

public class CellLabel extends JLabel {
    private boolean isPredefined = false;
    private int row;
    private int col;

    public CellLabel(String text, int row, int col) {
        super(text);
        this.row = row;
        this.col = col;
    }

    public boolean isPredefined() {
        return isPredefined;
    }

    public void setPredefined(boolean isPredefined) {
        this.isPredefined = isPredefined;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
