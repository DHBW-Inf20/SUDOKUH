package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LabelPanel extends JPanel {
    private final CellLabel label;
    private State state;
    public enum State {
        LABEL, PANEL
    }
    private boolean isPredefined = false;
    private final int row;
    private final int col;
    private final int size;
    String labelValue;
    private ArrayList<Integer> notes;
    private Color primaryTextColor;

    public LabelPanel(CellLabel lab, int row, int col, int size, Color primTextColor) {
        label = lab;
        state = State.LABEL;
        this.row = row;
        this.col = col;
        this.size = size;
        notes = new ArrayList<>();
        this.setLayout(new GridLayout(size, size));
        primaryTextColor = primTextColor;
    }

    public void setNote(int value) {
        if (!(this.labelValue == null)) {
            if (!(this.labelValue.equals(""))) {
                this.removeAll();
                this.labelValue = "";
            }
        }
        if (notes.contains(value)) return;
        if (state == State.LABEL) setNoteMode();

        final String stringValue = Integer.toString(value);
        JLabel label = new JLabel(stringValue);
        label.setForeground(primaryTextColor);
        if (notes.isEmpty()) {
            this.add(label, 0);
            notes.add(value);
            return;
        }
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i) > value) {
                this.add(label, i);
                notes.add(i, value);
                return;
            }
        }
        this.add(label, -1);
        notes.add(notes.size() - 1, value);
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

    public CellLabel getLabel() {
        return label;
    }

    public void setNormalMode() {
        state = State.LABEL;
    }

    public void setNoteMode() {
        state = State.PANEL;
    }

    public void setText(String value) {
        this.removeAll();
        notes = new ArrayList<>();
        for (int i = 0; i < size / 2; i++) {
            this.add(new JLabel());
        }
        this.add(label);
        label.setText(value);
        this.labelValue = value;
    }
}
