package view.ingame;

import view.Theme;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Kremling
 */
public final class CellPanel extends JPanel {

    // mutable state
    private boolean isPredefined = false;
    private String cellText = null;
    private final List<JLabel> noteLabels = new ArrayList<>();
    private JLabel killerSumLabel = null;

    private final JLabel cellTextLabel;
    private final int row;
    private final int column;
    private final Theme theme;
    private final int subGridSize;


    public CellPanel(int row, int column, int subGridSize, Theme theme, boolean withBorder) {
        setCellLayout();

        this.row = row;
        this.column = column;
        this.subGridSize = subGridSize;
        this.theme = theme;

        cellTextLabel = new JLabel(" ");
        cellTextLabel.setFont(new Font(getFont().getName(), Font.BOLD, 25));
        cellTextLabel.setBackground(theme.normalCellColor);
        cellTextLabel.setForeground(theme.primaryTextColor);
        cellTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cellTextLabel.setVerticalAlignment(SwingConstants.CENTER);

        setOpaque(true);
        setBackground(theme.normalCellColor);
        setForeground(theme.primaryTextColor);

        if (withBorder) {
            setBorder(new LineBorder(theme.cellBorderColor, 1));
        }
    }


    private void setNoteLayout() {
        setLayout(new GridLayout(subGridSize, subGridSize));
    }

    private void setCellLayout() {
        setLayout(new BorderLayout());
    }

    private void removeNotes() {
        for (JLabel noteLabel : noteLabels) {
            remove(noteLabel);
        }
        noteLabels.clear();
    }

    /**
     * Adds or removes a note to/from the cell.
     *
     * @param value value of the note
     */
    public void addOrRemoveNote(int value) {
        if (isPredefined) return;

        if (cellText != null) {
            removeNotes();
            cellText = null;
        }

        String stringValue = Integer.toString(value);
        setNoteLayout();

        int index = -1;
        boolean addNew = true;
        boolean addLast = true;
        for (JLabel noteLabel : noteLabels) {
            index++;
            String name = noteLabel.getName();

            if (name.equals(stringValue)) {
                remove(noteLabel);
                noteLabels.remove(index);
                addNew = false;
                break;
            }

            if (Integer.parseInt(name) > value) {
                addLast = false;
                break;
            }
        }
        if (addNew) {
            JLabel label = new JLabel(stringValue);
            label.setName(stringValue);
            label.setForeground(theme.primaryTextColor);
            add(label, addLast ? -1 : index);
            if (addLast) noteLabels.add(label);
            else noteLabels.add(index, label);
        }

        repaint();
        revalidate();
    }

    public boolean isPredefined() {
        return isPredefined;
    }

    /**
     * Marks the cell as predefined with {@code value}.
     */
    public void setPredefined(int value) {
        isPredefined = true;
        setBackground(theme.predefinedCellColor);
        setForeground(theme.primaryTextColor);
        setCellText(Integer.toString(value));
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
    public int getColumn() {
        return column;
    }

    public void setCellTextColor(Color color) {
        cellTextLabel.setForeground(color);
    }

    /**
     * @return the current value of the label of the cell
     */
    public String getCellText() {
        return cellText;
    }

    /**
     * Sets a text in the cell
     *
     * @param value the value to be set
     */
    public void setCellText(String value) {
        removeNotes();
        setCellLayout();
        add(cellTextLabel, BorderLayout.CENTER);
        cellTextLabel.setText(value);
        cellText = value;
        repaint();
        revalidate();
    }

    public void addKillerSumLabel(int sum) {
        killerSumLabel = new JLabel(Integer.toString(sum));
        killerSumLabel.setForeground(theme.primaryTextColor);
        add(killerSumLabel, BorderLayout.NORTH);
    }

    public void removeKillerSumLabel() {
        if (killerSumLabel != null) {
            remove(killerSumLabel);
            repaint();
            revalidate();
            killerSumLabel = null;
        }
    }
}
