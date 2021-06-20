package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static java.lang.Math.sqrt;
import static java.util.Arrays.*;
import static java.util.Collections.reverse;
import static java.util.Collections.shuffle;

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
        if (subGridSize < 0) {
            throw new IllegalArgumentException("subGridSize has to be positive or 0!");
        }
        this.subGridSize = subGridSize;
        gridSize = subGridSize * subGridSize;
        grid = new int[gridSize][gridSize];
        for (final int[] row : grid) {
            fill(row, EMPTY_CELL);
        }
    }

    public Sudoku(final int subGridSize, final Random random) {
        this(subGridSize);
        if (random == null) {
            throw new NullPointerException("You can not pass null for random!");
        }
        if (!solveInRandomOrder(random)) {
            throw new IllegalStateException("Could not solve empty sudoku, something is wrong!");
        }
    }

    public Sudoku(final int[][] grid) {
        this.gridSize = grid.length;
        subGridSize = (int) sqrt(gridSize);

        if (subGridSize * subGridSize != gridSize) {
            throw new IllegalArgumentException("Input sudoku did not have a size that is a square number!");
        }

        this.grid = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = copyOf(grid[i], grid[i].length);
        }
        if (!isValid()) {
            throw new IllegalArgumentException("Input sudoku is not valid!");
        }
    }


    public Sudoku getCopy() {
        return new Sudoku(grid);
    }


    // package-private for tests
    int[][] getGrid() {
        return grid;
    }

    public int getSubGridSize() {
        return subGridSize;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getNumberOfCells() {
        return gridSize * gridSize;
    }


    public int getCell(final int row, final int column) {
        return grid[row][column];
    }

    public void resetCell(final int row, final int column) {
        setCell(row, column, EMPTY_CELL);
    }

    public boolean setCell(final int row, final int column, final int value) {

        // empty cell is always ok
        if (value == EMPTY_CELL) {
            grid[row][column] = EMPTY_CELL;
            return true;
        }

        // invalid number -> don't check further
        if (value < 1 || value > gridSize) {
            return false;
        }

        final int previousCellValue = grid[row][column];
        grid[row][column] = value;
        if (constraintsFulfilled(row, column)) {
            return true;
        } else {
            grid[row][column] = previousCellValue; // undo setting invalid number
            return false;
        }
    }


    public boolean solve() {
        return solveInternal(getNextEmptyCell(0, 0), false, null);
    }

    public boolean solveInReverseOrder() {
        return solveInternal(getNextEmptyCell(0, 0), true, null);
    }

    // package-private for tests
    boolean solveInRandomOrder(final Random random) {
        return solveInternal(getNextEmptyCell(0, 0), false, random);
    }

    private boolean solveInternal(final Cell currentCell, final boolean inReverseOrder, final Random random) {

        if (currentCell == null) {
            return true; // all cells are filled (only valid fills happen) -> found solution
        }

        // on overflow: back to 0
        final int nextCellColumn = (currentCell.column + 1 < gridSize) ? currentCell.column + 1 : 0;
        final int nextCellRow = (nextCellColumn == 0) ? currentCell.row + 1 : currentCell.row;
        final Cell nextEmptyCell = getNextEmptyCell(nextCellRow, nextCellColumn); // start with next cell (current is empty)

        final List<Integer> numbers = new ArrayList<>(gridSize);

        for (int number = 1; number <= gridSize; number++) {
            numbers.add(number);
        }

        if (random != null) {
            shuffle(numbers, random);
        } else if (inReverseOrder) {
            reverse(numbers);
        }

        for (final int number : numbers) {
            grid[currentCell.row][currentCell.column] = number; // choose next number

            if (constraintsFulfilled(currentCell.row, currentCell.column) && // number was valid and
                    solveInternal(nextEmptyCell, inReverseOrder, random)) {  // recursive solve was successful
                return true;                                                 // -> found solution
            }
        }

        grid[currentCell.row][currentCell.column] = EMPTY_CELL; // no number was valid -> undo and go back in recursion
        return false;
    }

    private Cell getNextEmptyCell(final int startRow, final int startColumn) {
        // start row with startRow
        for (int row = startRow; row < gridSize; row++) {

            // start column with startColumn (but only in first iteration of outer loop)
            for (int column = (row == startRow ? startColumn : 0); column < gridSize; column++) {

                if (grid[row][column] == EMPTY_CELL) {
                    return new Cell(row, column);
                }
            }
        }
        return null; // reached end and did not find an empty cell
    }

    private boolean constraintsFulfilled(final int row, final int column) {

        // check for appearance of grid[row][column] in same row/column -> if one was found immediately return false
        for (int index = 0; index < gridSize; index++) {
            if ((row != index && grid[row][column] == grid[index][column]) ||     // grid[row][column] twice in row
                    (column != index && grid[row][column] == grid[row][index])) { // grid[row][column] twice in column
                return false;
            }
        }

        // sudoku is split into sub-grids: row % subGridSize maps row to value between 0 and subGridSize - 1
        // -> row % subGridSize is the row-index in its sub-grid
        // -> row - (row % subGridSize) is the row-index of the first cell in the sub-grid relative to the whole grid
        final int rowLowerBoundInclusive = row - (row % subGridSize);
        // the upper exclusive bound of a sub-grid is subGridSize higher than its lower inclusive bound
        final int rowUpperBoundExclusive = rowLowerBoundInclusive + subGridSize;
        // same for column
        final int columnLowerBoundInclusive = column - (column % subGridSize);
        final int columnUpperBoundExclusive = columnLowerBoundInclusive + subGridSize;

        for (int rowIndex = rowLowerBoundInclusive; rowIndex < rowUpperBoundExclusive; rowIndex++) {
            for (int columnIndex = columnLowerBoundInclusive; columnIndex < columnUpperBoundExclusive; columnIndex++) {

                // don't check grid[row][column] == grid[row][column] (always true)
                if (!(row == rowIndex && column == columnIndex) && grid[row][column] == grid[rowIndex][columnIndex]) {
                    return false; // grid[row][column] twice in sub-grid
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

                if (currentCell < 1 || currentCell > gridSize || !constraintsFulfilled(row, column)) {
                    return false;
                }
            }
        }
        return true; // no rule was violated -> valid
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sudoku sudoku = (Sudoku) o;
        return subGridSize == sudoku.subGridSize && gridSize == sudoku.gridSize && deepEquals(grid, sudoku.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(subGridSize, gridSize);
        result = 31 * result + deepHashCode(grid);
        return result;
    }

    @Override
    public String toString() {
        return "Sudoku{" +
                "grid=" + deepToString(grid) +
                ", subGridSize=" + subGridSize +
                ", gridSize=" + gridSize +
                '}';
    }
}
