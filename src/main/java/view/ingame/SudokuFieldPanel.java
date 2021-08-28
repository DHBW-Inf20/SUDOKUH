package view.ingame;

import model.AbstractPuzzle.Cell;
import model.Str8ts;
import util.GameMode;
import view.Theme;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static model.Str8ts.Color.BLACK;
import static model.Str8ts.Color.WHITE;
import static util.GameMode.STR8TS_SOLVE;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public class SudokuFieldPanel extends JPanel {

    private final Theme theme;

    /**
     * Reference to all {@link LabelPanel fields} of the sudoku game.
     */
    private final ArrayList<ArrayList<LabelPanel>> labels;

    /**
     * List of colors of each cell
     */
    private final ArrayList<ArrayList<Str8ts.Color>> colors;

    /**
     * Reference to the actual clicked {@link LabelPanel field} of the sudoku game.
     */
    private LabelPanel clicked;

    /**
     * Reference to an invalid {@link JLabel cell} if there is one.
     */
    private JLabel invalid;

    /**
     * List of conflicting cells.
     */
    private Set<Cell> conflicts;

    /**
     * The size of the grid.
     */
    private final int size;

    /**
     * The actual played gamemode.
     */
    private final GameMode gamemode;

    /**
     * Defines wether highlighting of possible conflicting cell should be active or not.
     */
    private final boolean highlighting;

    /**
     * The inputs set by the user
     */
    private ArrayList<LabelPanel> inputs;

    private final ArrayList<ArrayList<LabelPanel>> groups;

    private boolean chooseGroup;

    private boolean editGroup;

    private ArrayList<LabelPanel> group;

    public SudokuFieldPanel(int gridSize, Theme theme, boolean highlighting, GameMode gamemode) {
        this.theme = theme;

        this.gamemode = gamemode;

        conflicts = new HashSet<>();

        inputs = new ArrayList<>();

        this.highlighting = highlighting;

        size = gridSize;

        colors = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            ArrayList<Str8ts.Color> temp = new ArrayList<>();
            for (int j = 0; j < size * size; j++) {
                temp.add(WHITE);
            }
            colors.add(temp);
        }

        groups = new ArrayList<>();
        chooseGroup = false;
        editGroup = false;

        setLayout(new GridLayout(gridSize, gridSize));
        setSize(400, 400);
        setPreferredSize(new Dimension(400, 400));
        setBackground(theme.normalCellColor);
        setForeground(theme.primaryTextColor);

        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        labels = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            ArrayList<LabelPanel> temp = new ArrayList<>();
            for (int j = 0; j < gridSize * gridSize; j++) {
                JLabel field = new JLabel(" ");
                field.setFont(new Font(getFont().getName(), Font.BOLD, 25));
                LabelPanel labelPanel = new LabelPanel(field, i, j, gridSize, theme.primaryTextColor);
                labelPanel.setOpaque(true);
                labelPanel.setBackground(theme.normalCellColor);
                labelPanel.setForeground(theme.primaryTextColor);
                field.setBackground(theme.normalCellColor);
                field.setForeground(theme.primaryTextColor);
                if (gamemode != GameMode.KILLER_SOLVE) labelPanel.setBorder(new LineBorder(theme.cellBorderColor, 1));
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
                panel.setBackground(theme.normalCellColor);
                panel.setForeground(theme.primaryTextColor);
                panel.setLayout(new GridLayout(gridSize, gridSize));
                if (gamemode != STR8TS_SOLVE) panel.setBorder(new LineBorder(theme.cellBorderColor, 2));
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
                add(jPanel);
            }
        }

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = labels.get(0).get(0);
        clicked.setBackground(theme.clickedCellColor);

        setBounds(20, 120, 800, 800);
        setBackground(theme.panelBackgroundColor);
    }

    /**
     * Handling of clicking on a cell
     *
     * @param labelPanel Panel of cells
     */
    protected void handleClickEvent(LabelPanel labelPanel) {
        switch (gamemode) {
            case SUDOKU_SOLVE, KILLER_SOLVE -> {
                // Unmarking of possible conflicting cells
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getCol();
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                int rowUpperBound = rowLowerBound + size - 1;
                int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                int columnUpperBound = columnLowerBound + size - 1;
                if (highlighting) {
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(clickedRow).get(k).setBackground(theme.normalCellColor);
                        if (inputs.contains(labels.get(clickedRow).get(k)))
                            labels.get(clickedRow).get(k).setBackground(theme.predefinedCellColor);
                        labels.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                    }
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(k).get(clickedCol).setBackground(theme.normalCellColor);
                        if (inputs.contains(labels.get(k).get(clickedCol)))
                            labels.get(k).get(clickedCol).setBackground(theme.predefinedCellColor);
                        labels.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                    }
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            labels.get(k).get(l).setBackground(theme.normalCellColor);
                            if (inputs.contains(labels.get(k).get(l)))
                                labels.get(k).get(l).setBackground(theme.predefinedCellColor);
                            labels.get(k).get(l).setForeground(theme.primaryTextColor);
                        }
                    }
                }
                if (clicked != null) {
                    if (inputs.contains(clicked)) {
                        clicked.setBackground(theme.predefinedCellColor);
                    } else {
                        clicked.setBackground(theme.normalCellColor);
                    }
                    clicked.setForeground(theme.primaryTextColor);
                }
                clicked = labelPanel;
                // Marking of possible conflicting cells
                if (highlighting) {
                    clickedRow = clicked.getRow();
                    clickedCol = clicked.getCol();
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(clickedRow).get(k).setBackground(theme.markedCellColor);
                        if (inputs.contains(labels.get(clickedRow).get(k)))
                            labels.get(clickedRow).get(k).setBackground(theme.predefinedCellMarkedColor);
                        labels.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                    }
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(k).get(clickedCol).setBackground(theme.markedCellColor);
                        if (inputs.contains(labels.get(k).get(clickedCol)))
                            labels.get(k).get(clickedCol).setBackground(theme.predefinedCellMarkedColor);
                        labels.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                    }
                    rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                    rowUpperBound = rowLowerBound + size - 1;
                    columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                    columnUpperBound = columnLowerBound + size - 1;
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            labels.get(k).get(l).setBackground(theme.markedCellColor);
                            if (inputs.contains(labels.get(k).get(l)))
                                labels.get(k).get(l).setBackground(theme.predefinedCellMarkedColor);
                            labels.get(k).get(l).setForeground(theme.primaryTextColor);
                        }
                    }
                }
                clicked.setBackground(theme.clickedCellColor);
                clicked.setForeground(theme.primaryTextColor);
                addCellToGroup(clicked);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setText("");
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(theme.primaryTextColor);
                    }
                }
            }
            case STR8TS_SOLVE -> {
                // Unmarking of possible conflicting cells
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getCol();
                if (highlighting) {
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(clickedRow).get(k).setBackground(theme.normalCellColor);
                        if (inputs.contains(labels.get(clickedRow).get(k)))
                            labels.get(clickedRow).get(k).setBackground(theme.predefinedCellColor);
                        if (colors.get(clickedRow).get(k) == BLACK) {
                            labels.get(clickedRow).get(k).setBackground(theme.otherCellColor);
                            labels.get(clickedRow).get(k).getLabel().setForeground(theme.secondaryTextColor);
                        }
                    }
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(k).get(clickedCol).setBackground(theme.normalCellColor);
                        if (inputs.contains(labels.get(k).get(clickedCol)))
                            labels.get(k).get(clickedCol).setBackground(theme.predefinedCellColor);
                        if (colors.get(k).get(clickedCol) == BLACK) {
                            labels.get(k).get(clickedCol).setBackground(theme.otherCellColor);
                            labels.get(k).get(clickedCol).getLabel().setForeground(theme.secondaryTextColor);
                        }
                    }
                }
                if (clicked != null) {
                    if (colors.get(clickedRow).get(clickedCol) == BLACK) {
                        clicked.setBackground(theme.otherCellColor);
                        clicked.getLabel().setForeground(theme.secondaryTextColor);
                    } else if (inputs.contains(clicked)) {
                        clicked.setBackground(theme.predefinedCellColor);
                    } else {
                        clicked.setBackground(theme.normalCellColor);
                    }
                }
                clicked = labelPanel;
                // Marking of possible conflicting cells
                if (highlighting) {
                    clickedRow = clicked.getRow();
                    clickedCol = clicked.getCol();
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(clickedRow).get(k).setBackground(theme.markedCellColor);
                        if (inputs.contains(labels.get(clickedRow).get(k)))
                            labels.get(clickedRow).get(k).setBackground(theme.predefinedCellMarkedColor);
                        if (colors.get(clickedRow).get(k) == BLACK) {
                            labels.get(clickedRow).get(k).setBackground(theme.otherCellColor);
                            labels.get(clickedRow).get(k).getLabel().setForeground(theme.secondaryTextColor);
                        }
                    }
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(k).get(clickedCol).setBackground(theme.markedCellColor);
                        if (inputs.contains(labels.get(k).get(clickedCol)))
                            labels.get(k).get(clickedCol).setBackground(theme.predefinedCellMarkedColor);
                        if (colors.get(k).get(clickedCol) == BLACK) {
                            labels.get(k).get(clickedCol).setBackground(theme.otherCellColor);
                            labels.get(k).get(clickedCol).getLabel().setForeground(theme.secondaryTextColor);
                        }
                    }
                }
                clicked.setBackground(theme.clickedCellColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setText("");
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(theme.primaryTextColor);
                    }
                }
            }
            default -> {
                /*
                 *  Unmarking of previous highlighted cells
                 */
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getCol();
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                int rowUpperBound = rowLowerBound + size - 1;
                int columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                int columnUpperBound = columnLowerBound + size - 1;
                if (highlighting) {
                    // Unmarking the cells in the same row
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(clickedRow).get(k).setBackground(theme.normalCellColor);
                        labels.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                        if (labels.get(clickedRow).get(k).isPredefined())
                            labels.get(clickedRow).get(k).setBackground(theme.predefinedCellColor);
                    }
                    // Unmarking the cells in the same column
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(k).get(clickedCol).setBackground(theme.normalCellColor);
                        labels.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                        if (labels.get(k).get(clickedCol).isPredefined())
                            labels.get(k).get(clickedCol).setBackground(theme.predefinedCellColor);
                    }
                    // Unmarking the cells in the same subgrid
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            labels.get(k).get(l).setBackground(theme.normalCellColor);
                            labels.get(k).get(l).setForeground(theme.primaryTextColor);
                            if (labels.get(k).get(l).isPredefined())
                                labels.get(k).get(l).setBackground(theme.predefinedCellColor);
                        }
                    }
                }
                // Unmarking the clicked cell
                if (clicked != null) {
                    if (clicked.isPredefined()) {
                        clicked.setBackground(theme.predefinedCellColor);
                    } else {
                        clicked.setBackground(theme.normalCellColor);
                    }
                    clicked.setForeground(theme.primaryTextColor);
                }
                clicked = labelPanel;
                // Marking of possible conflicting cells
                clickedRow = clicked.getRow();
                clickedCol = clicked.getCol();
                // Highlighting the cells in the same row
                if (highlighting) {
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(clickedRow).get(k).setBackground(theme.markedCellColor);
                        if (labels.get(clickedRow).get(k).isPredefined())
                            labels.get(clickedRow).get(k).setBackground(theme.predefinedCellMarkedColor);
                        labels.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                    }
                    // Highlighting the cells in the same column
                    for (int k = 0; k < labels.get(clickedRow).size(); k++) {
                        labels.get(k).get(clickedCol).setBackground(theme.markedCellColor);
                        if (labels.get(k).get(clickedCol).isPredefined())
                            labels.get(k).get(clickedCol).setBackground(theme.predefinedCellMarkedColor);
                        labels.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                    }
                    // Highlighting the cells in the same subgrid
                    rowLowerBound = clicked.getRow() - (clicked.getRow() % size);
                    rowUpperBound = rowLowerBound + size - 1;
                    columnLowerBound = clicked.getCol() - (clicked.getCol() % size);
                    columnUpperBound = columnLowerBound + size - 1;
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            labels.get(k).get(l).setBackground(theme.markedCellColor);
                            if (labels.get(k).get(l).isPredefined())
                                labels.get(k).get(l).setBackground(theme.predefinedCellMarkedColor);
                            labels.get(k).get(l).setForeground(theme.primaryTextColor);
                        }
                    }
                }
                // Highlighting the clicked cell
                clicked.setBackground(theme.clickedCellColor);
                clicked.setForeground(theme.primaryTextColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setText("");
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(theme.primaryTextColor);
                    }
                }
            }
        }
    }

    /**
     * @return the actual clicked cell
     */
    public LabelPanel getClicked() {
        return clicked;
    }

    /**
     * Set the clicked cell to specified coordinates
     *
     * @param row    the cell-row
     * @param column the cell-column
     */
    public void setClicked(int row, int column) {
        LabelPanel newClicked = labels.get(row).get(column);
        handleClickEvent(newClicked);
    }

    /**
     * Sets a value to a cell of specified coordinates
     *
     * @param row   the cell-row
     * @param col   the cell-column
     * @param value the value to be set
     */
    public void setValue(int row, int col, int value) {
        if (gamemode == GameMode.KILLER_SOLVE) {
            labels.get(row).get(col).setKillerText(Integer.toString(value));
        } else {
            labels.get(row).get(col).setText(Integer.toString(value));
        }
        switch (gamemode) {
            case SUDOKU_SOLVE, KILLER_SOLVE -> {
                labels.get(row).get(col).setForeground(theme.primaryTextColor);
                if (inputs.contains(labels.get(row).get(col)))
                    labels.get(row).get(col).setBackground(theme.predefinedCellColor);
            }
            case STR8TS_SOLVE -> {
                if (colors.get(row).get(col) == BLACK) {
                    labels.get(row).get(col).setForeground(theme.secondaryTextColor);
                } else {
                    labels.get(row).get(col).setForeground(theme.primaryTextColor);
                    if (inputs.contains(labels.get(row).get(col)))
                        labels.get(row).get(col).setBackground(theme.predefinedCellColor);
                }
            }
            default -> labels.get(row).get(col).setForeground(theme.primaryTextColor);
        }
    }

    /**
     * Deletes the value of the actual clicked cell
     */
    public void resetCell() {
        if (gamemode == GameMode.SUDOKU_SOLVE) {
            inputs.remove(clicked);
        }
        clicked.setText("");
    }

    /**
     * Sets an input to the clicked cell and set the text color correctly
     *
     * @param input the input value from type String
     */
    public void validInput(String input) {
        switch (gamemode) {
            case SUDOKU_SOLVE -> {
                clicked.setForeground(theme.primaryTextColor);
                clicked.getLabel().setForeground(theme.primaryTextColor);
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(theme.primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
                inputs.add(clicked);
                clicked.setText(input);
            }
            case STR8TS_SOLVE -> {
                if (colors.get(clicked.getRow()).get(clicked.getCol()) == BLACK) {
                    clicked.setForeground(theme.secondaryTextColor);
                    clicked.getLabel().setForeground(theme.secondaryTextColor);
                } else {
                    clicked.setForeground(theme.primaryTextColor);
                    clicked.getLabel().setForeground(theme.primaryTextColor);
                }
                inputs.add(clicked);
                clicked.setText(input);
            }
            case KILLER_SOLVE -> {
                clicked.setForeground(theme.primaryTextColor);
                clicked.getLabel().setForeground(theme.primaryTextColor);
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(theme.primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
                clicked.setKillerText(input);
            }
            default -> {
                clicked.setForeground(theme.primaryTextColor);
                clicked.getLabel().setForeground(theme.primaryTextColor);
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        labels.get(c.row()).get(c.column()).getLabel().setForeground(theme.primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
                clicked.setText(input);
            }
        }
        invalid = null;
    }

    /**
     * Sets an invalid input (color: {@code errorTextColor}) to the clicked cell
     *
     * @param input the input value from type String
     */
    public void invalidInput(String input) {
        if (gamemode == GameMode.KILLER_SOLVE) {
            clicked.setKillerText(input);
        } else {
            clicked.setText(input);
        }
        clicked.setForeground(theme.errorTextColor);
        clicked.getLabel().setForeground(theme.errorTextColor);
        invalid = clicked.getLabel();
    }

    /**
     * Sets an invalid input to a specified cell when a conflict is created by using the tip function
     *
     * @param row the cell-row
     * @param col the cell-column
     */
    public void invalidInput(int row, int col) {
        labels.get(row).get(col).setForeground(theme.errorTextColor);
        labels.get(row).get(col).getLabel().setForeground(theme.errorTextColor);
        invalid = labels.get(row).get(col).getLabel();
    }

    /**
     * Sets the color of a conflicting cell to red and adds it to the conflicts-list
     *
     * @param c the invalid cell
     */
    public void highlightConflicts(Cell c) {
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
        labels.get(row).get(col).setBackground(theme.predefinedCellColor);
        labels.get(row).get(col).setForeground(theme.primaryTextColor);
    }

    /**
     * Sets a note to the clicked cell
     */
    public void setNote(int value) {
        clicked.setNote(value);
        repaint();
        revalidate();
    }

    /**
     * Change the color of the actual clicked cell
     */
    public Str8ts.Color changeColor() {
        int row = clicked.getRow();
        int col = clicked.getCol();
        Str8ts.Color color = colors.get(row).get(col);
        if (color == null) {
            return WHITE;
        }
        return switch (color) {
            case WHITE -> {
                labels.get(row).get(col).setBackground(theme.otherCellColor);
                labels.get(row).get(col).getLabel().setForeground(theme.secondaryTextColor);
                colors.get(row).set(col, BLACK);
                yield BLACK;
            }
            case BLACK -> {
                labels.get(row).get(col).setBackground(theme.normalCellColor);
                labels.get(row).get(col).getLabel().setForeground(theme.primaryTextColor);
                colors.get(row).set(col, WHITE);
                yield WHITE;
            }
        };
    }

    /**
     * Adds a group of cells to a group
     *
     * @param labels an arraylist of the {@link LabelPanel cells}
     * @param sum    the sum of the group
     */
    public void addGroup(ArrayList<LabelPanel> labels, int sum) {
        boolean condition = true;
        groups.add(labels);
        for (LabelPanel l : labels) {
            ArrayList<Boolean> neighbors = checkForNeighbors(l, labels);
            int top = 0, left = 0, bottom = 0, right = 0;
            for (int i = 0; i < neighbors.size(); i++) {
                switch (i) {
                    case 0 -> top = neighbors.get(i) ? 0 : 3;
                    case 1 -> right = neighbors.get(i) ? 0 : 3;
                    case 2 -> bottom = neighbors.get(i) ? 0 : 3;
                    case 3 -> left = neighbors.get(i) ? 0 : 3;
                }
            }
            l.setBorder(new MatteBorder(top, left, bottom, right, Color.decode("#5ba122")));
            if (condition) {
                JLabel sumLabel = new JLabel(String.valueOf(sum));
                sumLabel.setForeground(theme.primaryTextColor);
                l.add(sumLabel, BorderLayout.NORTH);
                condition = false;
            }
        }
    }

    /**
     * Removes a group completely
     *
     * @param cell the group of this cell will be deleted
     */
    public ArrayList<LabelPanel> removeGroup(LabelPanel cell) {
        int groupIndex = -1;
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<LabelPanel> list = groups.get(i);
            if (list.contains(cell)) {
                groupIndex = i;
                break;
            }
        }
        if (groupIndex != -1) {
            for (LabelPanel l : groups.get(groupIndex)) {
                l.setBorder(null);
                l.removeAll();
                l.setText(l.getLabelValue());
            }
            return groups.remove(groupIndex);
        }
        return null;
    }

    /**
     * Checks if a specified cell has direct neighbors in a list of cells.
     *
     * @param label  the cell to be checked
     * @param labels the list of the cells with whom should be checked
     * @return an array list that specifies whether the cell has a neighbor or not (0 = up, 1 = right, 2 = down, 3 = left)
     */
    private ArrayList<Boolean> checkForNeighbors(LabelPanel label, ArrayList<LabelPanel> labels) {
        int row = label.getRow();
        int col = label.getCol();
        ArrayList<Boolean> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            result.add(false);
        }
        for (LabelPanel labelPanel : labels) {
            int rowdif = row - labelPanel.getRow();
            int coldif = col - labelPanel.getCol();
            if (row == labelPanel.getRow() && coldif == 1) result.set(3, true);
            if (row == labelPanel.getRow() && coldif == -1) result.set(1, true);
            if (col == labelPanel.getCol() && rowdif == 1) result.set(0, true);
            if (col == labelPanel.getCol() && rowdif == -1) result.set(2, true);
        }
        return result;
    }

    /**
     * Sets choose mode to true
     */
    public void setChooseMode() {
        chooseGroup = true;
        group = new ArrayList<>();
        addCellToGroup(clicked);
    }

    /**
     * Sets choose mode to false
     *
     * @return the actual chosen group
     */
    public ArrayList<LabelPanel> setNoChooseMode() {
        chooseGroup = false;
        inputs = new ArrayList<>();
        for (LabelPanel l : group) {
            l.setBackground(theme.normalCellColor);
        }
        clicked.setBackground(theme.clickedCellColor);
        return group;
    }

    /**
     * Sets edit mode to true
     */
    public void setEditMode(ArrayList<LabelPanel> group) {
        editGroup = true;
        for (LabelPanel l : group) {
            l.setBackground(theme.predefinedCellColor);
            inputs.add(l);
        }
        this.group = group;
        groups.remove(this.group);
    }

    /**
     * Sets edit mode to false
     *
     * @return the actual chosen group
     */
    public ArrayList<LabelPanel> setNoEditMode() {
        editGroup = false;
        inputs = new ArrayList<>();
        for (LabelPanel l : group) {
            l.setBackground(theme.normalCellColor);
        }
        clicked.setBackground(theme.clickedCellColor);
        return group;
    }

    private void addCellToGroup(LabelPanel cell) {
        boolean isExisting = false;
        for (ArrayList<LabelPanel> l : groups) {
            if (l.contains(cell)) {
                isExisting = true;
                break;
            }
        }
        if ((chooseGroup || editGroup) & !isExisting) {
            if (group.isEmpty()) {
                group.add(cell);
                inputs.add(cell);
                cell.setBackground(theme.predefinedCellColor);
                cell.setForeground(theme.primaryTextColor);
            } else {
                if (group.contains(cell)) {
                    group.remove(cell);
                    inputs.remove(cell);
                    cell.setBackground(theme.normalCellColor);
                    cell.setForeground(theme.primaryTextColor);
                } else {
                    ArrayList<Boolean> hasNeighborList = checkForNeighbors(cell, group);
                    boolean hasNeighbor = false;
                    for (boolean b : hasNeighborList) {
                        if (b) {
                            hasNeighbor = true;
                            break;
                        }
                    }
                    if (hasNeighbor) {
                        group.add(cell);
                        inputs.add(cell);
                        cell.setBackground(theme.predefinedCellColor);
                        cell.setForeground(theme.primaryTextColor);
                    }
                }
            }
        }
    }
}
