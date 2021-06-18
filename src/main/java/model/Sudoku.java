package model;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

public class Sudoku {

    public static final int EMPTY_CELL = 0;
    public static final int DEFAULT_SUB_GRID_SIZE = 3;
    public static final int DEFAULT_GRID_SIZE = DEFAULT_SUB_GRID_SIZE * DEFAULT_SUB_GRID_SIZE;


    private static record Cell(int row, int column) {}


    private final int[][] grid;
    private final int subGridSize;
    private final int gridSize;


    public Sudoku() {
        this(DEFAULT_SUB_GRID_SIZE);
    }

    public Sudoku(final int subGridSize) {
        this.subGridSize = subGridSize;
        gridSize = subGridSize * subGridSize;
        grid = new int[gridSize][gridSize];
        for (final int[] row : grid) {
            fill(row, EMPTY_CELL);
        }
    }

    public Sudoku(final int[][] grid) throws IllegalArgumentException {
        this.gridSize = grid.length;
        subGridSize = (int) Math.sqrt(gridSize);
        this.grid = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = copyOf(grid[i], grid[i].length);
        }
        if (!isValid()) {
            throw new IllegalArgumentException("input sudoku is not valid");
        }
    }


    public int[][] getGrid() {
        return grid;
    }

    public int getSubGridSize() {
        return subGridSize;
    }

    public int getGridSize() {
        return gridSize;
    }


    public void resetCell(final int row, final int column) {
        setCell(row, column, EMPTY_CELL);
    }

    public boolean setCell(final int row, final int column, final int value) {
        if (value != EMPTY_CELL && value > gridSize) {
            return false;
        }
        final int previous = grid[row][column];
        grid[row][column] = value;
        if (constraintsFulfilled(new Cell(row, column))) {
            return true;
        } else {
            grid[row][column] = previous;
            return false;
        }
    }


    public boolean solve() {
        return solveInternal(getNextEmptyCell(0, 0));
    }

    private boolean solveInternal(final Cell currentCell) {

        if (currentCell == null) {
            return true; // all cells are filled (only valid fills happen) -> found solution
        }

        // on overflow: back to 0
        final int nextCellColumn = (currentCell.column + 1 < gridSize) ? currentCell.column + 1 : 0;
        final int nextCellRow = (nextCellColumn == 0) ? currentCell.row + 1 : currentCell.row;
        final Cell nextEmptyCell = getNextEmptyCell(nextCellRow, nextCellColumn); // start with next cell (current is empty)

        for (int number = 1; number <= gridSize; number++) {
            grid[currentCell.row][currentCell.column] = number; // choose next number

            // number was valid and recursive solve was successful -> found solution
            if (constraintsFulfilled(currentCell) && solveInternal(nextEmptyCell)) {
                return true;
            }
        }

        grid[currentCell.row][currentCell.column] = EMPTY_CELL; // no number was valid -> undo and go back in recursion
        return false;
    }

    private Cell getNextEmptyCell(final int startRow, final int startColumn) {
        // start row with startRow
        for (int row = startRow; row < gridSize; row++) {

            // start column with startColumn (but only in first iteration of outer loop)
            for (int column = (row == startRow) ? startColumn : 0; column < gridSize; column++) {

                if (grid[row][column] == EMPTY_CELL) {
                    return new Cell(row, column);
                }
            }
        }
        return null; // startRow >= GRID_SIZE or reached end
    }

    private boolean constraintsFulfilled(final Cell cell) {

        // check for appearance of grid[row][column] in same row/column -> if one was found immediately return false
        for (int index = 0; index < gridSize; index++) {
            if ((cell.row != index && grid[cell.row][cell.column] == grid[index][cell.column]) ||     // grid[row][column] twice in row
                    (cell.column != index && grid[cell.row][cell.column] == grid[cell.row][index])) { // grid[row][column] twice in column
                return false;
            }
        }

        // row % SUB_GRID_SIZE maps row to value between 0 and SUB_GRID_SIZE - 1 (sudoku is split into sub-grids)
        // the upper bound of one sub-grid is SUB_GRID_SIZE - 1 higher than lower bound
        // same for column
        final int rowLowerBound = cell.row - (cell.row % subGridSize);
        final int rowUpperBound = rowLowerBound + subGridSize - 1;
        final int columnLowerBound = cell.column - (cell.column % subGridSize);
        final int columnUpperBound = columnLowerBound + subGridSize - 1;

        for (int rowIndex = rowLowerBound; rowIndex <= rowUpperBound; rowIndex++) {
            for (int columnIndex = columnLowerBound; columnIndex <= columnUpperBound; columnIndex++) {

                if (!(cell.row == rowIndex && cell.column == columnIndex) &&           // don't check grid[row][column]
                        grid[cell.row][cell.column] == grid[rowIndex][columnIndex]) { // grid[row][column] twice in sub-grid
                    return false;
                }
            }
        }

        return true; // no rule was violated -> constraints fulfilled
    }

    private boolean isValid() {
        if (grid.length != gridSize) {
            return false; // wrong amount of rows
        }

        for (int row = 0; row < gridSize; row++) {
            if (grid[row].length != gridSize) {
                return false; // wrong amount of columns
            }

            for (int column = 0; column < gridSize; column++) {
                final int currentCell = grid[row][column];

                if (currentCell == EMPTY_CELL) {
                    continue; // empty cell is always ok
                }

                if (currentCell < 1 || currentCell > gridSize || !constraintsFulfilled(new Cell(row, column))) {
                    return false;
                }
            }
        }
        return true; // no rule was violated -> valid
    }
}
