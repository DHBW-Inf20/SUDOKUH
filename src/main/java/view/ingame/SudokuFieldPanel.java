package view.ingame;

import model.AbstractPuzzle;
import model.Str8ts;
import util.Mode;
import util.Themes;
import view.CellLabel;
import view.CustomButton;
import view.LabelPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SudokuFieldPanel extends JPanel {
    /**
     * Specifies whether the node mode is active or not
     */
    private boolean noteMode;

    /**
     * Reference to all {@link LabelPanel fields} of the sudoku game.
     */
    private ArrayList<ArrayList<LabelPanel>> labels;

    /**
     * List of colors of each cell
     */
    private final ArrayList<ArrayList<model.Str8ts.Color>> colors;

    /**
     * Reference to the actual clicked {@link LabelPanel field} of the sudoku game.
     */
    private LabelPanel clicked;

    /**
     * Various {@link Color colors} for the game menu.
     */
    // Color for field-background
    private final Color primaryBackgroundColor;
    // Color for field-background (secondary)
    private final Color secondaryBackgroundColor;
    // Color for field-background when clicked
    private final Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    private final Color markedColor;
    // Color for field-background when field is predefined
    private final Color predefinedColor;
    // Color for field-background when field is predefined and possible conflicting to field
    private final Color predefinedMarkedColor;
    // Color for field-borders
    private final Color borderColor;
    // Color for text
    private final Color primaryTextColor;
    // Color for text (secondary)
    private final Color secondaryTextColor;
    // Color for panel background
    private final Color panelBackgroundColor;
    // Color for text if there is an error
    private final Color errorTextColor;

    /**
     * Reference to an invalid {@link CellLabel cell} if there is one.
     */
    private CellLabel invalid;
    /**
     * List of conflicting cells.
     */
    private Set<AbstractPuzzle.Cell> conflicts;

    /**
     * The size of the grid.
     */
    private static int size;

    /**
     * The actual played gamemode.
     */
    private static util.Mode gamemode;

    /**
     * The actual played gamemode.
     */
    private ArrayList<view.LabelPanel> inputs;

    public SudokuFieldPanel(int gridSize, String theme, util.Mode gamemode){
        noteMode = false;

        Themes t = new Themes(theme);
        primaryBackgroundColor = t.getPrimaryBackgroundColor();
        secondaryBackgroundColor = t.getSecondaryBackgroundColor();
        clickedColor = t.getClickedColor();
        markedColor = t.getMarkedColor();
        predefinedColor = t.getPredefinedColor();
        predefinedMarkedColor = t.getPredefinedMarkedColor();
        primaryTextColor = t.getPrimaryTextColor();
        secondaryTextColor = t.getSecondaryTextColor();
        borderColor = t.getBorderColor();
        errorTextColor = t.getErrorTextColor();
        panelBackgroundColor = t.getPanelBackgroundColor();

        this.gamemode = gamemode;

        conflicts = new HashSet<>();

        inputs = new ArrayList<>();

        size = gridSize;

        colors = new ArrayList<>();
        for (int i = 0; i < size*size; i++) {
            ArrayList<model.Str8ts.Color> temp = new ArrayList<>();
            for (int j = 0; j < size*size; j++) {
                temp.add(model.Str8ts.Color.WHITE);
            }
            colors.add(temp);
        }

        this.setLayout(new GridLayout(gridSize, gridSize));
        this.setSize(400,400);
        this.setPreferredSize(new Dimension(400,400));
        this.setBackground(primaryBackgroundColor);
        this.setForeground(primaryTextColor);

        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        labels = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            ArrayList<LabelPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize * gridSize; j++) {
                CellLabel field = new CellLabel(" ");
                LabelPanel labelPanel = new LabelPanel(field,i,j,gridSize, primaryTextColor);
                labelPanel.setOpaque(true);
                labelPanel.setBackground(primaryBackgroundColor);
                labelPanel.setForeground(primaryTextColor);
                field.setBackground(primaryBackgroundColor);
                field.setForeground(primaryTextColor);
                labelPanel.setBorder(new LineBorder(borderColor, 1));
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setVerticalAlignment(SwingConstants.CENTER);
                labelPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleClickEvent(labelPanel);
                    }
                });
                temp.add(labelPanel);
            }
            labels.add(temp);
        }

        // Initializing matrix of panels ('panels') -> Creation in advance in order to get the right coordinates
        ArrayList<ArrayList<JPanel>> panels = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            ArrayList<JPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize; j++) {
                JPanel panel = new JPanel();
                panel.setBackground(primaryBackgroundColor);
                panel.setForeground(primaryTextColor);
                panel.setLayout(new GridLayout(gridSize, gridSize));
                panel.setBorder(new LineBorder(borderColor, 2));
                temp.add(panel);
            }
            panels.add(temp);
        }

        // Filling the panels-matrix with values from the labels-matrix
        for (int row = 0; row < gridSize * gridSize; row++) {
            for (int col = 0; col < gridSize * gridSize; col++) {
                LabelPanel field = labels.get(row).get(col);
                panels.get(row / gridSize).get(col / gridSize).add(field);
            }
        }

        // Filling the outerPanel-matrix (game overlay) with values from the panels-matrix
        for (ArrayList<JPanel> panel : panels) {
            for (JPanel jPanel : panel) {
                this.add(jPanel);
            }
        }

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = labels.get(0).get(0);
        clicked.setBackground(clickedColor);

        this.setBounds(20, 120,800,800);
        this.setBackground(panelBackgroundColor);
    }

    /**
     * Handling of clicking on a cell
     *
     * @param labelPanel Panel of cells
     */
    protected void handleClickEvent(LabelPanel labelPanel) {
        switch(gamemode) {
            case SUDOKU_SOLVE -> {
                // Unmarking of possible conflicting cells
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getCol();
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(clickedRow).get(k).setBackground(primaryBackgroundColor);
                    if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedColor);
                    labels.get(clickedRow).get(k).setForeground(primaryTextColor);
                }
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(k).get(clickedCol).setBackground(primaryBackgroundColor);
                    if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedColor);
                    labels.get(k).get(clickedCol).setForeground(primaryTextColor);
                }
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                int rowUpperBound = rowLowerBound + size - 1;
                int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                int columnUpperBound = columnLowerBound + size - 1;
                for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                    for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                        labels.get(k).get(l).setBackground(primaryBackgroundColor);
                        if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedColor);
                        labels.get(k).get(l).setForeground(primaryTextColor);
                    }
                }
                if (clicked != null) {
                    if(inputs.contains(clicked)) {
                        clicked.setBackground(predefinedColor);
                    } else {
                        clicked.setBackground(primaryBackgroundColor);
                    }
                    clicked.setForeground(primaryTextColor);
                }
                clicked = labelPanel;
                // Marking of possible conflicting cells
                clickedRow = clicked.getRow();
                clickedCol = clicked.getCol();
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(clickedRow).get(k).setBackground(markedColor);
                    if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
                    labels.get(clickedRow).get(k).setForeground(primaryTextColor);
                }
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(k).get(clickedCol).setBackground(markedColor);
                    if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
                    labels.get(k).get(clickedCol).setForeground(primaryTextColor);
                }
                rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                rowUpperBound = rowLowerBound + size - 1;
                columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                columnUpperBound = columnLowerBound + size - 1;
                for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                    for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                        labels.get(k).get(l).setBackground(markedColor);
                        if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                        labels.get(k).get(l).setForeground(primaryTextColor);
                    }
                }
                clicked.setBackground(clickedColor);
                clicked.setForeground(primaryTextColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setText("");
                if(! conflicts.isEmpty()) {
                    for(AbstractPuzzle.Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
                    }
                }
            }
            case STRAITS_SOLVE -> {
                // Unmarking of possible conflicting cells
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getCol();
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(clickedRow).get(k).setBackground(primaryBackgroundColor);
                    if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedColor);
                    if(colors.get(clickedRow).get(k) == Str8ts.Color.BLACK) {
                        labels.get(clickedRow).get(k).setBackground(secondaryBackgroundColor);
                        labels.get(clickedRow).get(k).getLabel().setForeground(secondaryTextColor);
                    }
                }
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(k).get(clickedCol).setBackground(primaryBackgroundColor);
                    if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedColor);
                    if(colors.get(k).get(clickedCol) == Str8ts.Color.BLACK) {
                        labels.get(k).get(clickedCol).setBackground(secondaryBackgroundColor);
                        labels.get(k).get(clickedCol).getLabel().setForeground(secondaryTextColor);
                    }
                }
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                int rowUpperBound = rowLowerBound + size - 1;
                int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                int columnUpperBound = columnLowerBound + size - 1;
                for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                    for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                        labels.get(k).get(l).setBackground(primaryBackgroundColor);
                        if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedColor);
                        if(colors.get(k).get(l) == Str8ts.Color.BLACK) {
                            labels.get(k).get(l).setBackground(secondaryBackgroundColor);
                            labels.get(k).get(l).getLabel().setForeground(secondaryTextColor);
                        }
                    }
                }
                if (clicked != null) {
                    if (colors.get(clickedRow).get(clickedCol) == Str8ts.Color.BLACK) {
                        clicked.setBackground(secondaryBackgroundColor);
                        clicked.getLabel().setForeground(secondaryTextColor);
                    } else if (inputs.contains(clicked)) {
                        clicked.setBackground(predefinedColor);
                    } else {
                        clicked.setBackground(primaryBackgroundColor);
                    }
                }
                clicked = labelPanel;
                // Marking of possible conflicting cells
                clickedRow = clicked.getRow();
                clickedCol = clicked.getCol();
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(clickedRow).get(k).setBackground(markedColor);
                    if(inputs.contains(labels.get(clickedRow).get(k))) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
                    if(colors.get(clickedRow).get(k) == Str8ts.Color.BLACK) {
                        labels.get(clickedRow).get(k).setBackground(secondaryBackgroundColor);
                        labels.get(clickedRow).get(k).getLabel().setForeground(secondaryTextColor);
                    }
                }
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(k).get(clickedCol).setBackground(markedColor);
                    if(inputs.contains(labels.get(k).get(clickedCol))) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
                    if(colors.get(k).get(clickedCol) == Str8ts.Color.BLACK) {
                        labels.get(k).get(clickedCol).setBackground(secondaryBackgroundColor);
                        labels.get(k).get(clickedCol).getLabel().setForeground(secondaryTextColor);
                    }
                }
                rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                rowUpperBound = rowLowerBound + size - 1;
                columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                columnUpperBound = columnLowerBound + size - 1;
                for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                    for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                        labels.get(k).get(l).setBackground(markedColor);
                        if(inputs.contains(labels.get(k).get(l))) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                        if(colors.get(k).get(l) == Str8ts.Color.BLACK) {
                            labels.get(k).get(l).setBackground(secondaryBackgroundColor);
                            labels.get(k).get(l).getLabel().setForeground(secondaryTextColor);
                        }
                    }
                }
                clicked.setBackground(clickedColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setText("");
                if(! conflicts.isEmpty()) {
                    for(AbstractPuzzle.Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
                    }
                }
            }
            default -> {
                /*
                 *  Unmarking of previous highlighted cells
                 */
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getCol();
                // Unmarking the cells in the same row
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(clickedRow).get(k).setBackground(primaryBackgroundColor);
                    labels.get(clickedRow).get(k).setForeground(primaryTextColor);
                    if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedColor);
                }
                // Unmarking the cells in the same column
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(k).get(clickedCol).setBackground(primaryBackgroundColor);
                    labels.get(k).get(clickedCol).setForeground(primaryTextColor);
                    if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedColor);
                }
                // Unmarking the cells in the same subgrid
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                int rowUpperBound = rowLowerBound + size - 1;
                int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                int columnUpperBound = columnLowerBound + size - 1;
                for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                    for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                        labels.get(k).get(l).setBackground(primaryBackgroundColor);
                        labels.get(k).get(l).setForeground(primaryTextColor);
                        if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedColor);
                    }
                }
                // Unmarking the clicked cell
                if (clicked != null) {
                    if (clicked.isPredefined()) {
                        clicked.setBackground(predefinedColor);
                    } else {
                        clicked.setBackground(primaryBackgroundColor);
                    }
                    clicked.setForeground(primaryTextColor);
                }
                clicked = labelPanel;
                // Marking of possible conflicting cells
                clickedRow = clicked.getRow();
                clickedCol = clicked.getCol();
                // Highlighting the cells in the same row
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(clickedRow).get(k).setBackground(markedColor);
                    if (labels.get(clickedRow).get(k).isPredefined()) labels.get(clickedRow).get(k).setBackground(predefinedMarkedColor);
                    labels.get(clickedRow).get(k).setForeground(primaryTextColor);
                }
                // Highlighting the cells in the same column
                for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                    labels.get(k).get(clickedCol).setBackground(markedColor);
                    if (labels.get(k).get(clickedCol).isPredefined()) labels.get(k).get(clickedCol).setBackground(predefinedMarkedColor);
                    labels.get(k).get(clickedCol).setForeground(primaryTextColor);
                }
                // Highlighting the cells in the same subgrid
                rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                rowUpperBound = rowLowerBound + size - 1;
                columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                columnUpperBound = columnLowerBound + size - 1;
                for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                    for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                        labels.get(k).get(l).setBackground(markedColor);
                        if (labels.get(k).get(l).isPredefined()) labels.get(k).get(l).setBackground(predefinedMarkedColor);
                        labels.get(k).get(l).setForeground(primaryTextColor);
                    }
                }
                // Highlighting the clicked cell
                clicked.setBackground(clickedColor);
                clicked.setForeground(primaryTextColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setText("");
                if(! conflicts.isEmpty()) {
                    for(model.AbstractPuzzle.Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
                    }
                }
            }
        }
    }

    /**
     * @return the actual clicked cell
     */
    public LabelPanel getClicked() { return clicked; }

    /**
     * Set the clicked cell to specified coordinates
     *
     * @param row the cell-row
     * @param column the cell-column
     */
    public void setClicked(int row, int column){
        LabelPanel newClicked = labels.get(row).get(column);
        handleClickEvent(newClicked);
    }

    /**
     * Sets a value to a cell of specified coordinates
     *
     * @param row the cell-row
     * @param col the cell-column
     * @param value the value to be set
     */
    public void setValue(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        switch(gamemode) {
            case SUDOKU_SOLVE -> {
                labels.get(row).get(col).setForeground(primaryTextColor);
                if(inputs.contains(labels.get(row).get(col))) labels.get(row).get(col).setBackground(predefinedColor);
            }
            case STRAITS_SOLVE -> {
                if(colors.get(row).get(col) == model.Str8ts.Color.BLACK) {
                    labels.get(row).get(col).setForeground(secondaryTextColor);
                } else {
                    labels.get(row).get(col).setForeground(primaryTextColor);
                    if(inputs.contains(labels.get(row).get(col))) labels.get(row).get(col).setBackground(predefinedColor);
                }
            }
            default -> {
                labels.get(row).get(col).setForeground(primaryTextColor);
            }
        }
    }

    /**
     * Deletes the value of the actual clicked cell
     */
    public void resetCell() {
        switch(gamemode) {
            case SUDOKU_PLAY -> {
                clicked.setText("");
            }
            case SUDOKU_SOLVE -> {
                clicked.setText("");
                inputs.remove(clicked);
            }
            default -> {
                clicked.getLabel().setText("");
            }
        }
    }

    /**
     * Sets an input to the clicked cell and set the text color correctly
     *
     * @param input the input value from type String
     */
    public void validInput(String input) {
        switch(gamemode) {
            case SUDOKU_SOLVE -> {
                clicked.setForeground(primaryTextColor);
                clicked.getLabel().setForeground(primaryTextColor);
                if(! conflicts.isEmpty()) {
                    for(model.AbstractPuzzle.Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
                inputs.add(clicked);
            }
            case STRAITS_SOLVE -> {
                if(colors.get(clicked.getRow()).get(clicked.getCol()) == model.Str8ts.Color.BLACK) {
                    clicked.setForeground(secondaryTextColor);
                    clicked.getLabel().setForeground(secondaryTextColor);
                } else {
                    clicked.setForeground(primaryTextColor);
                    clicked.getLabel().setForeground(primaryTextColor);
                }
                inputs.add(clicked);
            }
            default -> {
                clicked.setForeground(primaryTextColor);
                clicked.getLabel().setForeground(primaryTextColor);
                if(! conflicts.isEmpty()) {
                    for(model.AbstractPuzzle.Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
            }
        }
        clicked.setText(input);
        invalid = null;
    }

    /**
     * Sets an invalid input (color: {@code errorTextColor}) to the clicked cell
     *
     * @param input the input value from type String
     */
    public void invalidInput(String input) {
        clicked.setText(input);
        clicked.setForeground(errorTextColor);
        clicked.getLabel().setForeground(errorTextColor);
        invalid = clicked.getLabel();
    }

    /**
     * Sets an invalid input to a specified cell when a conflict is created by using the tip function
     *
     * @param row the cell-row
     * @param col the cell-column
     */
    public void invalidInput(int row, int col) {
        labels.get(row).get(col).setForeground(errorTextColor);
        labels.get(row).get(col).getLabel().setForeground(errorTextColor);
        invalid = labels.get(row).get(col).getLabel();
    }

    /**
     * Sets the color of a conflicting cell to red and adds it to the conflicts-list
     *
     * @param c the invalid cell
     */
    public void highlightConflicts(model.AbstractPuzzle.Cell c) {
        labels.get(c.row()).get(c.column()).getLabel().setForeground(Color.red);
        conflicts.add(c);
    }

    /**
     * @return the grid size
     */
    public int getGridSize() {
        return size;
    }

    /**
     * Marks a specific cell as predifined in order to cannot be changed and has another color
     */
    public void setPredefined(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setPredefined(true);
        labels.get(row).get(col).setBackground(predefinedColor);
        labels.get(row).get(col).setForeground(primaryTextColor);
    }

    /**
     * @return the actual set note mode
     */
    public boolean getNoteMode() {
        return noteMode;
    }

    /**
     * Sets a note to the clicked cell
     */
    public void setNote(int value) {
        clicked.setNote(value);
        this.revalidate();
    }

    /**
     * Changes the node mode and sets the color of the button
     */
    public void changeNoteMode() {
        noteMode = !noteMode;
        if (noteMode) {
            clicked.setNoteMode();
        } else {
            clicked.setNormalMode();
        }
    }

    /**
     * Change the color of the actual clicked cell
     */
    public void changeColor() {
        int row = clicked.getRow();
        int col = clicked.getCol();
        model.Str8ts.Color color = colors.get(row).get(col);
        switch(color) {
            case WHITE -> {
                labels.get(row).get(col).setBackground(secondaryBackgroundColor);
                labels.get(row).get(col).getLabel().setForeground(secondaryTextColor);
                colors.get(row).set(col, model.Str8ts.Color.BLACK);
            }
            case BLACK -> {
                labels.get(row).get(col).setBackground(primaryBackgroundColor);
                labels.get(row).get(col).getLabel().setForeground(primaryTextColor);
                colors.get(row).set(col, model.Str8ts.Color.WHITE);
            }
        }
    }
}
