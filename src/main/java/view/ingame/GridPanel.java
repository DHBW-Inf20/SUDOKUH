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
import static util.GameMode.KILLER_SOLVE;
import static util.GameMode.STR8TS_SOLVE;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public final class GridPanel extends JPanel {

    private final Theme theme;

    /**
     * Reference to all {@link CellPanel cells} of the sudoku game.
     */
    private final ArrayList<ArrayList<CellPanel>> cells;

    /**
     * List of colors of each cell
     */
    private final ArrayList<ArrayList<Str8ts.Color>> colors;

    /**
     * Reference to the currently clicked {@link CellPanel cell} of the sudoku game.
     */
    private CellPanel clicked;

    /**
     * Reference to an invalid {@link JLabel cell} if there is one.
     */
    private CellPanel invalid;

    /**
     * List of conflicting cells.
     */
    private Set<Cell> conflicts;

    /**
     * The size of the grid.
     */
    private final int subGridSize;

    /**
     * The currently played gamemode.
     */
    private final GameMode gamemode;

    /**
     * Defines wether highlighting of possible conflicting cell should be active or not.
     */
    private final boolean highlighting;

    /**
     * The inputs set by the user
     */
    private ArrayList<CellPanel> inputs;

    private final ArrayList<ArrayList<CellPanel>> groups;

    private boolean chooseGroupModeActivated;

    private boolean editGroup;

    private ArrayList<CellPanel> group;

    public GridPanel(int subGridSize, Theme theme, boolean highlighting, GameMode gamemode) {
        this.theme = theme;

        this.gamemode = gamemode;

        conflicts = new HashSet<>();

        inputs = new ArrayList<>();

        this.highlighting = highlighting;

        this.subGridSize = subGridSize;

        colors = new ArrayList<>();
        for (int i = 0; i < subGridSize * subGridSize; i++) {
            ArrayList<Str8ts.Color> temp = new ArrayList<>();
            for (int j = 0; j < subGridSize * subGridSize; j++) {
                temp.add(WHITE);
            }
            colors.add(temp);
        }

        groups = new ArrayList<>();
        chooseGroupModeActivated = false;
        editGroup = false;

        setLayout(new GridLayout(subGridSize, subGridSize));
        setSize(400, 400);
        setPreferredSize(new Dimension(400, 400));
        setBackground(theme.normalCellColor);
        setForeground(theme.primaryTextColor);

        // Creating matrix of Label-Elements ('labels') -> Creation in advance in order to get the right coordinates
        cells = new ArrayList<>();
        for (int i = 0; i < subGridSize * subGridSize; i++) {
            ArrayList<CellPanel> temp = new ArrayList<>();
            for (int j = 0; j < subGridSize * subGridSize; j++) {
                CellPanel cellPanel = new CellPanel(i, j, subGridSize, theme, gamemode != KILLER_SOLVE);
                cellPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleClickEvent(cellPanel);
                    }
                });
                temp.add(cellPanel);
            }
            cells.add(temp);
        }

        // Initializing matrix of panels ('panels') -> Creation in advance in order to get the right coordinates
        ArrayList<ArrayList<JPanel>> panels = new ArrayList<>();
        for (int i = 0; i < subGridSize; i++) {
            ArrayList<JPanel> temp = new ArrayList<>();
            for (int j = 0; j < subGridSize; j++) {
                JPanel panel = new JPanel();
                panel.setBackground(theme.normalCellColor);
                panel.setForeground(theme.primaryTextColor);
                panel.setLayout(new GridLayout(subGridSize, subGridSize));
                if (gamemode != STR8TS_SOLVE) panel.setBorder(new LineBorder(theme.cellBorderColor, 2));
                temp.add(panel);
            }
            panels.add(temp);
        }

        // Filling the panels-matrix with values from the labels-matrix
        for (int row = 0; row < subGridSize * subGridSize; row++) {
            for (int col = 0; col < subGridSize * subGridSize; col++) {
                CellPanel cell = cells.get(row).get(col);
                panels.get(row / subGridSize).get(col / subGridSize).add(cell);
            }
        }

        // Filling the outerPanel-matrix (game overlay) with values from the panels-matrix
        for (ArrayList<JPanel> panel : panels) {
            for (JPanel jPanel : panel) {
                add(jPanel);
            }
        }

        // Changing clicked-Value to 0|0 to exclude errors
        clicked = cells.get(0).get(0);
        clicked.setBackground(theme.clickedCellColor);

        setBounds(20, 120, 800, 800);
        setBackground(theme.panelBackgroundColor);
    }

    /**
     * Handling of clicking on a cell
     *
     * @param cellPanel Panel of cells
     */
    private void handleClickEvent(CellPanel cellPanel) {
        switch (gamemode) {
            case SUDOKU_SOLVE, KILLER_SOLVE -> {
                // Unmarking of possible conflicting cells
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getColumn();
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % subGridSize);
                int rowUpperBound = rowLowerBound + subGridSize - 1;
                int columnLowerBound = clicked.getColumn() - (clicked.getColumn() % subGridSize);
                int columnUpperBound = columnLowerBound + subGridSize - 1;
                if (highlighting) {
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(clickedRow).get(k).setBackground(theme.normalCellColor);
                        if (inputs.contains(cells.get(clickedRow).get(k)))
                            cells.get(clickedRow).get(k).setBackground(theme.predefinedCellColor);
                        cells.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                    }
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(k).get(clickedCol).setBackground(theme.normalCellColor);
                        if (inputs.contains(cells.get(k).get(clickedCol)))
                            cells.get(k).get(clickedCol).setBackground(theme.predefinedCellColor);
                        cells.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                    }
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            cells.get(k).get(l).setBackground(theme.normalCellColor);
                            if (inputs.contains(cells.get(k).get(l)))
                                cells.get(k).get(l).setBackground(theme.predefinedCellColor);
                            cells.get(k).get(l).setForeground(theme.primaryTextColor);
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
                clicked = cellPanel;
                // Marking of possible conflicting cells
                if (highlighting) {
                    clickedRow = clicked.getRow();
                    clickedCol = clicked.getColumn();
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(clickedRow).get(k).setBackground(theme.markedCellColor);
                        if (inputs.contains(cells.get(clickedRow).get(k)))
                            cells.get(clickedRow).get(k).setBackground(theme.predefinedCellMarkedColor);
                        cells.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                    }
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(k).get(clickedCol).setBackground(theme.markedCellColor);
                        if (inputs.contains(cells.get(k).get(clickedCol)))
                            cells.get(k).get(clickedCol).setBackground(theme.predefinedCellMarkedColor);
                        cells.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                    }
                    rowLowerBound = clicked.getRow() - (clicked.getRow() % subGridSize);
                    rowUpperBound = rowLowerBound + subGridSize - 1;
                    columnLowerBound = clicked.getColumn() - (clicked.getColumn() % subGridSize);
                    columnUpperBound = columnLowerBound + subGridSize - 1;
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            cells.get(k).get(l).setBackground(theme.markedCellColor);
                            if (inputs.contains(cells.get(k).get(l)))
                                cells.get(k).get(l).setBackground(theme.predefinedCellMarkedColor);
                            cells.get(k).get(l).setForeground(theme.primaryTextColor);
                        }
                    }
                }
                clicked.setBackground(theme.clickedCellColor);
                clicked.setForeground(theme.primaryTextColor);
                addCellToGroup(clicked);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setCellText("");
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        cells.get(c.row()).get(c.column()).setCellTextColor(theme.primaryTextColor);
                    }
                }
            }
            case STR8TS_SOLVE -> {
                // Unmarking of possible conflicting cells
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getColumn();
                if (highlighting) {
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(clickedRow).get(k).setBackground(theme.normalCellColor);
                        if (inputs.contains(cells.get(clickedRow).get(k)))
                            cells.get(clickedRow).get(k).setBackground(theme.predefinedCellColor);
                        if (colors.get(clickedRow).get(k) == BLACK) {
                            cells.get(clickedRow).get(k).setBackground(theme.otherCellColor);
                            cells.get(clickedRow).get(k).setCellTextColor(theme.secondaryTextColor);
                        }
                    }
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(k).get(clickedCol).setBackground(theme.normalCellColor);
                        if (inputs.contains(cells.get(k).get(clickedCol)))
                            cells.get(k).get(clickedCol).setBackground(theme.predefinedCellColor);
                        if (colors.get(k).get(clickedCol) == BLACK) {
                            cells.get(k).get(clickedCol).setBackground(theme.otherCellColor);
                            cells.get(k).get(clickedCol).setCellTextColor(theme.secondaryTextColor);
                        }
                    }
                }
                if (clicked != null) {
                    if (colors.get(clickedRow).get(clickedCol) == BLACK) {
                        clicked.setBackground(theme.otherCellColor);
                        clicked.setCellTextColor(theme.secondaryTextColor);
                    } else if (inputs.contains(clicked)) {
                        clicked.setBackground(theme.predefinedCellColor);
                    } else {
                        clicked.setBackground(theme.normalCellColor);
                    }
                }
                clicked = cellPanel;
                // Marking of possible conflicting cells
                if (highlighting) {
                    clickedRow = clicked.getRow();
                    clickedCol = clicked.getColumn();
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(clickedRow).get(k).setBackground(theme.markedCellColor);
                        if (inputs.contains(cells.get(clickedRow).get(k)))
                            cells.get(clickedRow).get(k).setBackground(theme.predefinedCellMarkedColor);
                        if (colors.get(clickedRow).get(k) == BLACK) {
                            cells.get(clickedRow).get(k).setBackground(theme.otherCellColor);
                            cells.get(clickedRow).get(k).setCellTextColor(theme.secondaryTextColor);
                        }
                    }
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(k).get(clickedCol).setBackground(theme.markedCellColor);
                        if (inputs.contains(cells.get(k).get(clickedCol)))
                            cells.get(k).get(clickedCol).setBackground(theme.predefinedCellMarkedColor);
                        if (colors.get(k).get(clickedCol) == BLACK) {
                            cells.get(k).get(clickedCol).setBackground(theme.otherCellColor);
                            cells.get(k).get(clickedCol).setCellTextColor(theme.secondaryTextColor);
                        }
                    }
                }
                clicked.setBackground(theme.clickedCellColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setCellText("");
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        cells.get(c.row()).get(c.column()).setCellTextColor(theme.primaryTextColor);
                    }
                }
            }
            default -> {
                /*
                 *  Unmarking of previous highlighted cells
                 */
                int clickedRow = clicked.getRow();
                int clickedCol = clicked.getColumn();
                int rowLowerBound = clicked.getRow() - (clicked.getRow() % subGridSize);
                int rowUpperBound = rowLowerBound + subGridSize - 1;
                int columnLowerBound = clicked.getColumn() - (clicked.getColumn() % subGridSize);
                int columnUpperBound = columnLowerBound + subGridSize - 1;
                if (highlighting) {
                    // Unmarking the cells in the same row
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(clickedRow).get(k).setBackground(theme.normalCellColor);
                        cells.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                        if (cells.get(clickedRow).get(k).isPredefined())
                            cells.get(clickedRow).get(k).setBackground(theme.predefinedCellColor);
                    }
                    // Unmarking the cells in the same column
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(k).get(clickedCol).setBackground(theme.normalCellColor);
                        cells.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                        if (cells.get(k).get(clickedCol).isPredefined())
                            cells.get(k).get(clickedCol).setBackground(theme.predefinedCellColor);
                    }
                    // Unmarking the cells in the same subgrid
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            cells.get(k).get(l).setBackground(theme.normalCellColor);
                            cells.get(k).get(l).setForeground(theme.primaryTextColor);
                            if (cells.get(k).get(l).isPredefined())
                                cells.get(k).get(l).setBackground(theme.predefinedCellColor);
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
                clicked = cellPanel;
                // Marking of possible conflicting cells
                clickedRow = clicked.getRow();
                clickedCol = clicked.getColumn();
                // Highlighting the cells in the same row
                if (highlighting) {
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(clickedRow).get(k).setBackground(theme.markedCellColor);
                        if (cells.get(clickedRow).get(k).isPredefined())
                            cells.get(clickedRow).get(k).setBackground(theme.predefinedCellMarkedColor);
                        cells.get(clickedRow).get(k).setForeground(theme.primaryTextColor);
                    }
                    // Highlighting the cells in the same column
                    for (int k = 0; k < cells.get(clickedRow).size(); k++) {
                        cells.get(k).get(clickedCol).setBackground(theme.markedCellColor);
                        if (cells.get(k).get(clickedCol).isPredefined())
                            cells.get(k).get(clickedCol).setBackground(theme.predefinedCellMarkedColor);
                        cells.get(k).get(clickedCol).setForeground(theme.primaryTextColor);
                    }
                    // Highlighting the cells in the same subgrid
                    rowLowerBound = clicked.getRow() - (clicked.getRow() % subGridSize);
                    rowUpperBound = rowLowerBound + subGridSize - 1;
                    columnLowerBound = clicked.getColumn() - (clicked.getColumn() % subGridSize);
                    columnUpperBound = columnLowerBound + subGridSize - 1;
                    for (int k = rowLowerBound; k <= rowUpperBound; k++) {
                        for (int l = columnLowerBound; l <= columnUpperBound; l++) {
                            cells.get(k).get(l).setBackground(theme.markedCellColor);
                            if (cells.get(k).get(l).isPredefined())
                                cells.get(k).get(l).setBackground(theme.predefinedCellMarkedColor);
                            cells.get(k).get(l).setForeground(theme.primaryTextColor);
                        }
                    }
                }
                // Highlighting the clicked cell
                clicked.setBackground(theme.clickedCellColor);
                clicked.setForeground(theme.primaryTextColor);

                // Reset the value of an invalid cell
                if (invalid != null) invalid.setCellText("");
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        cells.get(c.row()).get(c.column()).setCellTextColor(theme.primaryTextColor);
                    }
                }
            }
        }
    }

    /**
     * @return the currently clicked cell
     */
    public CellPanel getClicked() {
        return clicked;
    }

    /**
     * Set the clicked cell to specified coordinates
     *
     * @param row    the cell-row
     * @param column the cell-column
     */
    public void setClicked(int row, int column) {
        CellPanel newClicked = cells.get(row).get(column);
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
            cells.get(row).get(col).setCellText(Integer.toString(value));
        } else {
            cells.get(row).get(col).setCellText(Integer.toString(value));
        }
        switch (gamemode) {
            case SUDOKU_SOLVE, KILLER_SOLVE -> {
                cells.get(row).get(col).setForeground(theme.primaryTextColor);
                if (inputs.contains(cells.get(row).get(col)))
                    cells.get(row).get(col).setBackground(theme.predefinedCellColor);
            }
            case STR8TS_SOLVE -> {
                if (colors.get(row).get(col) == BLACK) {
                    cells.get(row).get(col).setForeground(theme.secondaryTextColor);
                } else {
                    cells.get(row).get(col).setForeground(theme.primaryTextColor);
                    if (inputs.contains(cells.get(row).get(col)))
                        cells.get(row).get(col).setBackground(theme.predefinedCellColor);
                }
            }
            default -> cells.get(row).get(col).setForeground(theme.primaryTextColor);
        }
    }

    /**
     * Deletes the value of the currently clicked cell
     */
    public void resetCell() {
        if (gamemode == GameMode.SUDOKU_SOLVE) {
            inputs.remove(clicked);
        }
        clicked.setCellText("");
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
                clicked.setCellTextColor(theme.primaryTextColor);
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        cells.get(c.row()).get(c.column()).setCellTextColor(theme.primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
                inputs.add(clicked);
                clicked.setCellText(input);
            }
            case STR8TS_SOLVE -> {
                if (colors.get(clicked.getRow()).get(clicked.getColumn()) == BLACK) {
                    clicked.setForeground(theme.secondaryTextColor);
                    clicked.setCellTextColor(theme.secondaryTextColor);
                } else {
                    clicked.setForeground(theme.primaryTextColor);
                    clicked.setCellTextColor(theme.primaryTextColor);
                }
                inputs.add(clicked);
                clicked.setCellText(input);
            }
            default -> {
                clicked.setForeground(theme.primaryTextColor);
                clicked.setCellTextColor(theme.primaryTextColor);
                if (!conflicts.isEmpty()) {
                    for (Cell c : conflicts) {
                        cells.get(c.row()).get(c.column()).setCellTextColor(theme.primaryTextColor);
                    }
                }
                conflicts = new HashSet<>();
                clicked.setCellText(input);
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
        clicked.setCellText(input);
        clicked.setForeground(theme.errorTextColor);
        clicked.setCellTextColor(theme.errorTextColor);
        invalid = clicked;
    }

    /**
     * Sets an invalid input to a specified cell when a conflict is created by using the tip function
     *
     * @param row the cell-row
     * @param col the cell-column
     */
    public void invalidInput(int row, int col) {
        cells.get(row).get(col).setForeground(theme.errorTextColor);
        cells.get(row).get(col).setCellTextColor(theme.errorTextColor);
        invalid = cells.get(row).get(col);
    }

    /**
     * Sets the color of a conflicting cell to red and adds it to the conflicts-list
     *
     * @param c the invalid cell
     */
    public void highlightConflicts(Cell c) {
        cells.get(c.row()).get(c.column()).setCellTextColor(theme.errorTextColor);
        conflicts.add(c);
    }

    /**
     * @return the grid size
     */
    public int getGridSize() {
        return subGridSize;
    }

    /**
     * Marks a specific cell as predifined in order to cannot be changed and has another color
     */
    public void setPredefined(int row, int col, int value) {
        cells.get(row).get(col).setPredefined(value);
    }

    /**
     * Adds or removes a note to/from the clicked cell.
     */
    public void addOrRemoveNote(int value) {
        clicked.addOrRemoveNote(value);
    }

    /**
     * Change the color of the currently clicked cell
     */
    public Str8ts.Color changeAndGetColor() {
        int row = clicked.getRow();
        int col = clicked.getColumn();
        Str8ts.Color color = colors.get(row).get(col);
        if (color == null) {
            return WHITE;
        }
        return switch (color) {
            case WHITE -> {
                cells.get(row).get(col).setBackground(theme.otherCellColor);
                cells.get(row).get(col).setCellTextColor(theme.secondaryTextColor);
                colors.get(row).set(col, BLACK);
                yield BLACK;
            }
            case BLACK -> {
                cells.get(row).get(col).setBackground(theme.normalCellColor);
                cells.get(row).get(col).setCellTextColor(theme.primaryTextColor);
                colors.get(row).set(col, WHITE);
                yield WHITE;
            }
        };
    }

    /**
     * Adds a group of cells to a group
     *
     * @param labels an arraylist of the {@link CellPanel cells}
     * @param sum    the sum of the group
     */
    public void addGroup(ArrayList<CellPanel> labels, int sum) {
        groups.add(labels);
        for (CellPanel cellPanel : labels) {
            ArrayList<Boolean> neighbors = checkForNeighbors(cellPanel, labels);
            int top = 0, left = 0, bottom = 0, right = 0;
            for (int i = 0; i < neighbors.size(); i++) {
                switch (i) {
                    case 0 -> top = neighbors.get(i) ? 0 : 3;
                    case 1 -> right = neighbors.get(i) ? 0 : 3;
                    case 2 -> bottom = neighbors.get(i) ? 0 : 3;
                    case 3 -> left = neighbors.get(i) ? 0 : 3;
                }
            }
            cellPanel.setBorder(new MatteBorder(top, left, bottom, right, theme.clickedCellColor));
        }
        if (labels.size() > 0) labels.get(0).addKillerSumLabel(sum);
    }

    /**
     * Removes a group completely
     *
     * @param cell the group of this cell will be deleted
     */
    public ArrayList<CellPanel> removeGroup(CellPanel cell) {
        int groupIndex = -1;
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<CellPanel> list = groups.get(i);
            if (list.contains(cell)) {
                groupIndex = i;
                break;
            }
        }
        if (groupIndex != -1) {
            for (CellPanel cellPanel : groups.get(groupIndex)) {
                cellPanel.setBorder(null);
                cellPanel.removeKillerSumLabel();
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
    private ArrayList<Boolean> checkForNeighbors(CellPanel label, ArrayList<CellPanel> labels) {
        int row = label.getRow();
        int col = label.getColumn();
        ArrayList<Boolean> result = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            result.add(false);
        }
        for (CellPanel cellPanel : labels) {
            int rowdif = row - cellPanel.getRow();
            int coldif = col - cellPanel.getColumn();
            if (row == cellPanel.getRow() && coldif == 1) result.set(3, true);
            if (row == cellPanel.getRow() && coldif == -1) result.set(1, true);
            if (col == cellPanel.getColumn() && rowdif == 1) result.set(0, true);
            if (col == cellPanel.getColumn() && rowdif == -1) result.set(2, true);
        }
        return result;
    }

    /**
     * Sets choose mode to true
     */
    public void startChooseGroupMode() {
        chooseGroupModeActivated = true;
        group = new ArrayList<>();
        addCellToGroup(clicked);
    }

    /**
     * Sets choose mode to false
     *
     * @return the currently chosen group
     */
    public ArrayList<CellPanel> endChooseGroupModeAndGetGroup() {
        chooseGroupModeActivated = false;
        inputs = new ArrayList<>();
        for (CellPanel cellPanel : group) {
            cellPanel.setBackground(theme.normalCellColor);
        }
        clicked.setBackground(theme.clickedCellColor);
        return group;
    }

    /**
     * Sets edit mode to true
     */
    public void startEditGroupMode(ArrayList<CellPanel> group) {
        editGroup = true;
        for (CellPanel cellPanel : group) {
            cellPanel.setBackground(theme.predefinedCellColor);
            inputs.add(cellPanel);
        }
        this.group = group;
        groups.remove(this.group);
    }

    /**
     * Sets edit mode to false
     *
     * @return the currently chosen group
     */
    public ArrayList<CellPanel> endEditGroupModeAndGetGroup() {
        editGroup = false;
        inputs = new ArrayList<>();
        for (CellPanel cellPanel : group) {
            cellPanel.setBackground(theme.normalCellColor);
        }
        clicked.setBackground(theme.clickedCellColor);
        return group;
    }

    private void addCellToGroup(CellPanel cell) {
        boolean isExisting = false;
        for (ArrayList<CellPanel> cellPanels : groups) {
            if (cellPanels.contains(cell)) {
                isExisting = true;
                break;
            }
        }
        if ((chooseGroupModeActivated || editGroup) & !isExisting) {
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
