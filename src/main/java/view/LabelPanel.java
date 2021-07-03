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

    public LabelPanel(CellLabel lab, int row, int col, int size) {
        label = lab;
        state = State.LABEL;
        this.row = row;
        this.col = col;
        this.size = size;
        notes = new ArrayList<>();
        this.setLayout(new GridLayout(size, size));
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
        if (notes.isEmpty()) {
            this.add(new JLabel(stringValue), 0);
            notes.add(value);
            System.out.println("added empty " + value);
            return;
        }
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i) > value) {
                this.add(new JLabel(stringValue), i);
                notes.add(i, value);
                System.out.println("added " + value + " to " + i);
                return;
            }
        }
        this.add(new JLabel(stringValue), -1);
        notes.add(notes.size() - 1, value);
        System.out.println("added full " + value);
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
