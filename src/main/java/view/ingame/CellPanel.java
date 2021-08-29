package view.ingame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Philipp Kremling
 */
public final class CellPanel extends JPanel {
    private final JLabel label;
    private State state;
    public enum State {
        LABEL, PANEL
    }
    private boolean isPredefined = false;
    private final int row;
    private final int col;
    String labelValue;
    private ArrayList<String> notes;
    private final Color primaryTextColor;
    int size;

    public CellPanel(JLabel lab, int row, int col, int size, Color primTextColor) {
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
     * @param val value of the note
     */
    public void setNote(int val) {
        String value = String.valueOf(val);
        this.setNoteMode();
        if (this.labelValue != null) {
            this.removeAll();
            this.labelValue = null;
        }
        if (notes.contains(value)) {
            Component[] cList = this.getComponents();
            for(Component c : cList) {
                if(c.getName().equals(value)) {
                    this.remove(c);
                    break;
                }
            }
            notes.remove(value);
            this.repaint();
            this.revalidate();
            return;
        }
        if (state == State.LABEL) setNoteMode();

        JLabel label = new JLabel(value);
        label.setName(value);
        label.setForeground(primaryTextColor);
        if (notes.isEmpty()) {
            this.add(label, 0);
            notes.add(value);
            return;
        }
        for (int i = 0; i < notes.size(); i++) {
            if (Integer.parseInt(notes.get(i)) > Integer.parseInt(value)) {
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
     * @return the current set label of the cell
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * @return the current value of the label of the cell
     */
    public String getLabelValue() {
        return labelValue;
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
        this.setNormalMode();
        this.removeAll();
        this.repaint();
        this.revalidate();
        notes = new ArrayList<>();
        this.add(label,BorderLayout.CENTER);
        label.setText(value);
        this.labelValue = value;
        this.repaint();
        this.revalidate();
    }

    /**
     * Sets a text in the object for gamemode Killer
     *
     * @param value the value to be set
     */
    public void setKillerText(String value) {
        this.setNormalMode();
        this.add(label,BorderLayout.CENTER);
        label.setText(value);
        this.labelValue = value;
        this.repaint();
        this.revalidate();
    }
}
