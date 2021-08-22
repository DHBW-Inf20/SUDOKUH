package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LabelPanel extends JPanel {
    private final JLabel label;
    private State state;
    public enum State {
        LABEL, PANEL
    }
    private boolean isPredefined = false;
    private final int row;
    private final int col;
    String labelValue;
    private ArrayList<Integer> notes;
    private final Color primaryTextColor;
    int size;

    public LabelPanel(JLabel lab, int row, int col, int size, Color primTextColor) {
        label = lab;
        state = State.LABEL;
        this.row = row;
        this.col = col;
        this.size = size;
        notes = new ArrayList<>();
        this.setLayout(new BorderLayout());
        primaryTextColor = primTextColor;
    }

    /**
     * Sets a note to the object
     *
     * @param value value of the note
     */
    public void setNote(int value) {
        if (this.labelValue != null) {
            this.removeAll();
            this.labelValue = null;
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

    /**
     * Marks the cell as predefined or not
     *
     * @param isPredefined wether the cell is predefined (true) or not (false)
     */
    public void setPredefined(boolean isPredefined) {
        this.isPredefined = isPredefined;
    }

    /**
     * @return the row of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column of the cell
     */
    public int getCol() {
        return col;
    }

    /**
     * @return the actual set label of the cell
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * Makes the cell ready for normal inputs
     */
    public void setNormalMode() {
        this.setLayout(new BorderLayout());
        state = State.LABEL;
    }

    /**
     * Makes the cell ready for notes
     */
    public void setNoteMode() {
        this.setLayout(new GridLayout(size, size));
        state = State.PANEL;
    }

    /**
     * Sets a text in the object
     *
     * @param value the value to be set
     */
    public void setText(String value) {
        this.removeAll();
        notes = new ArrayList<>();
        this.add(label,BorderLayout.CENTER);
        label.setText(value);
        this.labelValue = value;
    }
}
