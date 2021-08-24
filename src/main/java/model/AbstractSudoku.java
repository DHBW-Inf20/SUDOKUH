package model;

import java.util.Set;

import static java.lang.Math.sqrt;
import static java.util.Arrays.fill;
import static java.util.Objects.hash;
import static util.Arrays.deepCopyOf;
import static util.Checks.requireNonNegative;

/**
 * Abstract base class for all Sudoku-like puzzles like {@link Sudoku} and {@link Killer} with the additional constraint
 * of sub-grids with unique values.
 * <p>
 * This class provides the following public interface in addition to {@link AbstractPuzzle}:
 * <ul>
 * <li>{@link #getSubGridSize()} - for providing information about the sub-grids of an AbstractSudoku</li>
 * </ul>
 *
 * @author Luca Kellermann
 */
public abstract class AbstractSudoku extends AbstractPuzzle {

    protected final int subGridSize;


    protected AbstractSudoku(final int subGridSize) {
        super(new int[requireNonNegative(subGridSize, "subGridSize has to be positive or 0!") * subGridSize][subGridSize * subGridSize]);
        this.subGridSize = subGridSize;
        for (final int[] row : grid) {
            fill(row, EMPTY_CELL);
        }
    }

    protected AbstractSudoku(final int[][] grid) {
        super(deepCopyOf(grid));
        subGridSize = (int) sqrt(gridSize);
        if (subGridSize * subGridSize != gridSize) {
            throw new IllegalArgumentException("Input grid did not have a size that is a square number!");
        }
    }


    public final int getSubGridSize() {
        return subGridSize;
    }


    @Override
    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = super.getConflictingCells(row, column, getAll);
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

        // check for duplicates of current cell in sub-grid
        for (int rowIndex = rowLowerBoundInclusive; rowIndex < rowUpperBoundExclusive; rowIndex++) {
            for (int columnIndex = columnLowerBoundInclusive; columnIndex < columnUpperBoundExclusive; columnIndex++) {

                // don't check grid[row][column] == grid[row][column] (always true)
                if ((row != rowIndex || column != columnIndex) && grid[row][column] == grid[rowIndex][columnIndex]) {
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
    protected boolean isEqualTo(final AbstractPuzzle other) {
        return this.subGridSize == ((AbstractSudoku) other).subGridSize;
    }

    @Override
    public int hashCode() {
        return hash(super.hashCode(), subGridSize);
    }
}
