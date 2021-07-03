package model;

import java.util.Random;
import java.util.Set;

import static java.lang.Math.sqrt;
import static java.util.Arrays.deepToString;
import static java.util.Arrays.fill;
import static java.util.Objects.hash;
import static util.ArrayUtilities.deepCopyOf;

public final class Sudoku extends BasePuzzle {

    public static final int DEFAULT_SUB_GRID_SIZE = 3;
    public static final int DEFAULT_GRID_SIZE = DEFAULT_SUB_GRID_SIZE * DEFAULT_SUB_GRID_SIZE;


    private final int subGridSize;


    public Sudoku() {
        this(DEFAULT_SUB_GRID_SIZE);
    }

    public Sudoku(final int subGridSize) {
        super(new int[subGridSize * subGridSize][subGridSize * subGridSize], subGridSize * subGridSize);
        if (subGridSize < 0) {
            throw new IllegalArgumentException("subGridSize has to be positive or 0!");
        }
        this.subGridSize = subGridSize;
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

    // package-private for tests
    Sudoku(final int[][] grid) {
        super(deepCopyOf(grid), grid.length);
        subGridSize = (int) sqrt(gridSize);

        if (subGridSize * subGridSize != gridSize) {
            throw new IllegalArgumentException("Input sudoku did not have a size that is a square number!");
        }

        if (isInvalid()) {
            throw new IllegalArgumentException("Input sudoku is not valid!");
        }
    }


    public final Sudoku getCopy() {
        return new Sudoku(grid);
    }


    public final int getSubGridSize() {
        return subGridSize;
    }


    @Override
    protected final Cell getNextEmptyCellForSolve(final Cell startCell, final boolean inclusive) {
        final int startRow = startCell.row(), startColumn = startCell.column();
        boolean firstCell = true;

        // start row with startRow
        for (int row = startRow; row < gridSize; row++) {

            // start column with startColumn (but only in first iteration of outer loop)
            for (int column = (row == startRow ? startColumn : 0); column < gridSize; column++) {

                // skip the first cell if not inclusive
                if (firstCell) {
                    firstCell = false;
                    if (!inclusive) continue;
                }

                if (grid[row][column] == EMPTY_CELL) {
                    return new Cell(row, column);
                }
            }
        }
        return null; // reached end and did not find an empty cell
    }

    @Override
    protected final Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = getConflictingCellsInSameRowOrColumn(row, column, getAll);
        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
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
                    conflicts.add(new Cell(rowIndex, columnIndex)); // grid[row][column] twice in sub-grid
                    if (!getAll) {
                        return conflicts;
                    }
                }
            }
        }

        return conflicts;
    }


    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final Sudoku sudoku = (Sudoku) o;
        return subGridSize == sudoku.subGridSize;
    }

    @Override
    public final int hashCode() {
        return hash(super.hashCode(), subGridSize);
    }

    @Override
    public final String toString() {
        return "Sudoku{" +
                "subGridSize=" + subGridSize +
                ", gridSize=" + gridSize +
                ", grid=" + deepToString(grid) +
                '}';
    }
}
