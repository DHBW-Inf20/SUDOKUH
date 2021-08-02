package model;

import java.util.Set;

import static java.util.Arrays.*;
import static java.util.Objects.requireNonNull;
import static model.Str8ts.Color.BLACK;
import static model.Str8ts.Color.WHITE;
import static util.Arrays.deepCopyOf;
import static util.Arrays.twoLevelCopyOf;

public final class Str8ts extends AbstractPuzzle {

    /**
     * The color of a {@link model.AbstractPuzzle.Cell cell} in a Str8ts.
     * <p>Can only be {@link #BLACK black} or {@link #WHITE white}.</p>
     */
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


    /**
     * Creates an empty Str8ts with {@link #GRID_SIZE} for its {@link #getGridSize() gridSize}.
     * The {@link Color color} of every cell is set to {@link Color#WHITE white}.
     */
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

    // package-private constructor for tests
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


    /**
     * Returns the {@link Color color} of the cell in the specified {@code row} and {@code column}.
     */
    public Color getColor(final int row, final int column) {
        return colors[row][column];
    }

    /**
     * Trys to set the {@link Color color} of the cell in the specified {@code row} and {@code column}.
     * <p>This will not work if you try to change the color from {@link Color#BLACK black} to {@link Color#WHITE white}
     * and the cell value of the previously black cell (that was excluded from straights) makes a straight impossible.
     * </p>
     *
     * @return {@code true} if successful, {@code false} otherwise
     * @throws NullPointerException if {@code color} is {@code null}
     */
    public boolean setColor(final int row, final int column, final Color color) {

        if (colors[row][column] == requireNonNull(color)) {
            return true; // color did not change
        }

        return switch (color) {
            case WHITE -> {
                colors[row][column] = WHITE;
                if (getConflictingCells(row, column, false).isEmpty()) {
                    yield true;
                } else {
                    colors[row][column] = BLACK;
                    yield false;
                }
            }
            case BLACK -> {
                // black is always ok
                colors[row][column] = BLACK;
                yield true;
            }
        };
    }


    @Override
    protected Cell getNextEmptyCellForSolve(int startRow, int startColumn, final boolean inclusive) {

        Cell cell = super.getNextEmptyCellForSolve(startRow, startColumn, inclusive);

        // continuously get the next empty cell until its color is white (black cells must not be filled)
        while (cell != null) {
            startRow = cell.row();
            startColumn = cell.column();
            if (colors[startRow][startColumn] == WHITE) {
                return cell; // found a white empty cell
            }
            cell = super.getNextEmptyCellForSolve(startRow, startColumn, false);
        }

        return null; // reached end and did not find an empty white cell
    }


    @Override
    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = super.getConflictingCells(row, column, getAll);
        if ((!getAll && !conflicts.isEmpty()) || colors[row][column] == BLACK) {
            // black cells don't have to be in a straight so there cannot exist any further conflicts
            return conflicts;
        }

        // find limits of horizontal straight and add its conflicts to the set of conflicts
        final int straightRowStartInclusive = getLastWhiteIndex(row, column, false, true);
        final int straightRowEndInclusive = getLastWhiteIndex(row, column, true, true);
        addStraightConflictsTo(conflicts, straightRowStartInclusive, straightRowEndInclusive, row, column, true, getAll);

        if (!getAll && !conflicts.isEmpty()) {
            return conflicts;
        }

        // find limits of vertical straight and add its conflicts to the set of conflicts
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

    private void addStraightConflictsTo(final Set<Cell> conflicts, final int straightStartIndexInclusive,
                                        final int straightEndIndexInclusive, final int startIndex,
                                        final int otherIndex, final boolean inRow, final boolean addAll) {
        // only one cell -> always a straight
        if (straightStartIndexInclusive == straightEndIndexInclusive) {
            return;
        }

        // occurrences[n] == true -> cell value n + 1 already occurred in this straight
        final boolean[] occurrences = new boolean[gridSize];

        // fill occurrences
        for (int index = straightStartIndexInclusive; index <= straightEndIndexInclusive; index++) {
            final int cell = grid[inRow ? index : otherIndex][inRow ? otherIndex : index];

            // straight can not be evaluated yet
            if (cell == EMPTY_CELL) {
                return;
            }
            // - 1 since cell values are out of [1, gridSize] but indices are out of [0, gridSize - 1]
            occurrences[cell - 1] = true;
        }

        boolean alreadyOneOccurrence = false; // whether iteration over occurrences has seen true
        boolean reachedLastOccurrence = false; // whether iteration over occurrences has seen false after seeing true
        for (final boolean occurrence : occurrences) { // in order 1, 2, ..., gridSize

            if (occurrence) { // sees true

                if (reachedLastOccurrence) { // sees true and has seen false after true -> no straight

                    // add cells in this straight to conflicts (except current cell)
                    for (int index = straightStartIndexInclusive; index <= straightEndIndexInclusive; index++) {
                        if (index != startIndex) {
                            conflicts.add(new Cell(inRow ? index : otherIndex, inRow ? otherIndex : index));
                            if (!addAll) {
                                return;
                            }
                        }
                    }
                    return; // return after adding conflicts

                } else {
                    alreadyOneOccurrence = true; // has seen true
                }

            } else if (alreadyOneOccurrence) { // sees false and has seen true
                reachedLastOccurrence = true; // has seen false after true
            }
        }
    }


    @Override
    protected boolean hasToValidateBeforeSolve() {
        // there are no requirements for the structure that MUST be fulfilled in order to solve a Str8ts
        // (could e.g. also solve an empty Str8ts with only white colors)
        return false;
    }

    /**
     * Returns {@code false} if and only if {@link #grid} and {@link #colors} are squares with {@link #gridSize}
     * {@code *} {@link #gridSize} cells, every cell is either {@link #EMPTY_CELL} or in the valid range from {@code 1}
     * to {@link #gridSize} (both inclusive), every color is not {@code null} and there are no
     * {@link #getConflictingCells(int, int, boolean) conflicts}.
     */
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
