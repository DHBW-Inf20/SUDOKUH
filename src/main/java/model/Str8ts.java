package model;

import java.util.Set;

import static java.util.Arrays.*;
import static java.util.Objects.requireNonNull;
import static model.Str8ts.Color.BLACK;
import static model.Str8ts.Color.WHITE;
import static util.Arrays.deepCopyOf;
import static util.Arrays.twoLevelCopyOf;

public final class Str8ts extends AbstractPuzzle {

    public enum Color {
        BLACK, WHITE;

        @Override
        public final String toString() {
            return switch (this) {
                case BLACK -> "B";
                case WHITE -> "W";
            };
        }
    }


    public static final int GRID_SIZE = 9;


    private final Color[][] colors;


    public Str8ts() {
        super(new int[GRID_SIZE][GRID_SIZE], GRID_SIZE);
        for (final int[] row : grid) {
            fill(row, EMPTY_CELL);
        }
        colors = new Color[gridSize][gridSize];
        for (final Color[] row : colors) {
            fill(row, WHITE);
        }
    }

    // package-private for tests
    Str8ts(final int[][] grid, final Color[][] colors) {
        super(deepCopyOf(grid), grid.length);
        this.colors = twoLevelCopyOf(colors);
        if (gridSize != GRID_SIZE || isInvalid()) {
            throw new IllegalArgumentException("Input Str8ts is not valid!");
        }
    }


    @Override
    protected Str8ts getCopy() {
        return new Str8ts(grid, colors);
    }


    // package-private for tests
    Color[][] getColors() {
        return colors;
    }


    public Color getColor(final int row, final int column) {
        return colors[row][column];
    }

    public boolean setColor(final int row, final int column, final Color color) {

        if (colors[row][column] == requireNonNull(color)) {
            return true;
        }

        return switch (color) {
            case WHITE -> {
                colors[row][column] = color;
                if (getConflictingCells(row, column, false).isEmpty()) {
                    yield true;
                } else {
                    colors[row][column] = BLACK;
                    yield false;
                }
            }
            case BLACK -> {
                // black is always valid
                colors[row][column] = color;
                yield true;
            }
        };
    }


    @Override
    protected Cell getNextEmptyCellForSolve(int startRow, int startColumn, final boolean inclusive) {

        Cell cell = super.getNextEmptyCellForSolve(startRow, startColumn, inclusive);

        while (cell != null) {
            startRow = cell.row();
            startColumn = cell.column();
            if (colors[startRow][startColumn] == WHITE) {
                return cell;
            }
            cell = super.getNextEmptyCellForSolve(startRow, startColumn, false);
        }

        return null; // reached end and did not find an empty white cell
    }


    @Override
    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = super.getConflictingCells(row, column, getAll);
        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
        }

        // black cells don't have to be in a straight -> return already detected conflicts
        if (colors[row][column] == BLACK) {
            return conflicts;
        }

        final int straightRowStartInclusive = getLastWhiteIndex(row, column, false, true);
        final int straightRowEndInclusive = getLastWhiteIndex(row, column, true, true);
        addStraightConflictsTo(conflicts, straightRowStartInclusive, straightRowEndInclusive, row, column, true, getAll);

        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
        }

        final int straightColumnStartInclusive = getLastWhiteIndex(row, column, false, false);
        final int straightColumnEndInclusive = getLastWhiteIndex(row, column, true, false);
        addStraightConflictsTo(conflicts, straightColumnStartInclusive, straightColumnEndInclusive, column, row, false, getAll);

        return conflicts;
    }

    private int getLastWhiteIndex(final int startRow, final int startColumn, final boolean increment,
                                  final boolean inRow) {
        int result = inRow ? startRow : startColumn;

        while (increment ? ++result < gridSize : --result >= 0) {
            if (colors[inRow ? result : startRow][inRow ? startColumn : result] != WHITE) break;
        }

        return increment ? result - 1 : result + 1; // undo last inc-/decrement from while loop condition
    }

    private void addStraightConflictsTo(final Set<Cell> conflictsSoFar, final int straightStartIndexInclusive,
                                        final int straightEndIndexInclusive, final int startIndex,
                                        final int otherIndex, final boolean inRow, final boolean addAll) {
        // only one cell -> always a straight
        if (straightStartIndexInclusive == straightEndIndexInclusive) {
            return;
        }

        final boolean[] occurrences = new boolean[gridSize];

        for (int index = straightStartIndexInclusive; index <= straightEndIndexInclusive; index++) {
            final int cell = grid[inRow ? index : otherIndex][inRow ? otherIndex : index];

            // straight can not be evaluated yet
            if (cell == EMPTY_CELL) {
                return;
            }
            // - 1 since cell values are out of [1, gridSize] but indices are out of [0, gridSize - 1]
            occurrences[cell - 1] = true;
        }

        boolean alreadyOneOccurrence = false;
        boolean reachedLastOccurrence = false;
        for (final boolean occurrence : occurrences) { // in order 1, 2, ..., gridSize
            if (occurrence) {
                // occurrence after reaching seemingly last occurrence -> no straight
                if (reachedLastOccurrence) {
                    for (int index = straightStartIndexInclusive; index <= straightEndIndexInclusive; index++) {
                        if (index != startIndex) {
                            conflictsSoFar.add(new Cell(inRow ? index : otherIndex, inRow ? otherIndex : index));
                            if (!addAll) {
                                return;
                            }
                        }
                    }
                    return;
                } else {
                    alreadyOneOccurrence = true;
                }
            } else if (alreadyOneOccurrence) {
                reachedLastOccurrence = true;
            }
        }
    }


    @Override
    protected boolean hasToValidateBeforeSolve() {
        return false;
    }

    @Override
    protected boolean isInvalid() {

        if (colors.length != gridSize || super.isInvalid()) {
            return true; // wrong amount of rows in colors or grid is invalid
        }

        for (int row = 0; row < gridSize; row++) {
            if (colors[row].length != gridSize) {
                return true; // wrong amount of columns in colors
            }

            for (int column = 0; column < gridSize; column++) {
                if (colors[row][column] == null) {
                    return true; // null is not allowed in colors
                }
            }
        }

        return false; // no rule was violated -> valid
    }


    @Override
    protected boolean isEqualTo(final AbstractPuzzle other) {
        return deepEquals(this.colors, ((Str8ts) other).colors);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + deepHashCode(colors);
        return result;
    }

    @Override
    public String toString() {
        return "Str8ts{" +
                "gridSize=" + gridSize +
                ", grid=" + deepToString(grid) +
                ", colors=" + deepToString(colors) +
                '}';
    }
}
