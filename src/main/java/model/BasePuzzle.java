package model;

import java.util.*;

import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Collections.*;
import static java.util.Objects.hash;

public abstract class BasePuzzle {

    public static final int EMPTY_CELL = 0;


    public static final record Cell(int row, int column) {}

    public static final record SetResult(boolean isSuccess, Set<Cell> conflictingCells) {
        public static final SetResult SUCCESS = new SetResult(true, emptySet());
        public static final SetResult INVALID_VALUE = new SetResult(false, emptySet());
    }


    protected final int[][] grid;
    protected final int gridSize;


    protected BasePuzzle(final int[][] grid, final int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;
    }


    // package-private for tests
    final int[][] getGrid() {
        return grid;
    }

    public final int getGridSize() {
        return gridSize;
    }

    public final int getNumberOfCells() {
        return gridSize * gridSize;
    }


    public final int getCell(final int row, final int column) {
        return grid[row][column];
    }

    public final void resetCell(final int row, final int column) {
        setCell(row, column, EMPTY_CELL);
    }

    public final SetResult setCell(final int row, final int column, final int value) {

        // empty cell is always ok
        if (value == EMPTY_CELL) {
            grid[row][column] = EMPTY_CELL;
            return SetResult.SUCCESS;
        }

        // invalid value -> don't check further
        if (value < 1 || value > gridSize) {
            return SetResult.INVALID_VALUE;
        }

        final int previousCellValue = grid[row][column];
        grid[row][column] = value;

        final Set<Cell> conflictingCells = getConflictingCells(row, column, true);

        if (conflictingCells.isEmpty()) {
            return SetResult.SUCCESS;
        } else {
            grid[row][column] = previousCellValue; // undo setting invalid number
            return new SetResult(false, conflictingCells);
        }
    }


    public final boolean solve() {
        return solveInternal(getNextEmptyCellForSolve(0, 0, true), false, null);
    }

    public final boolean solveInReverseOrder() {
        return solveInternal(getNextEmptyCellForSolve(0, 0, true), true, null);
    }

    // package-private for tests
    final boolean solveInRandomOrder(final Random random) {
        return solveInternal(getNextEmptyCellForSolve(0, 0, true), false, random);
    }

    private boolean solveInternal(final Cell currentCell, final boolean inReverseOrder, final Random random) {

        if (currentCell == null) {
            return true; // all cells are filled (only valid fills happen) -> found solution
        }

        final Cell nextEmptyCell = getNextEmptyCellForSolveExcluding(currentCell);

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

            //        no conflicts          &&         recursive solve was successful                 -> found solution
            if (hasNoConflicts(currentCell) && solveInternal(nextEmptyCell, inReverseOrder, random)) {
                return true;
            }
        }

        // no number was valid -> undo and go back in recursion
        grid[currentCell.row][currentCell.column] = EMPTY_CELL;
        return false;
    }


    private Cell getNextEmptyCellForSolveExcluding(final Cell startCell) {
        return getNextEmptyCellForSolve(startCell.row, startCell.column, false);
    }

    protected Cell getNextEmptyCellForSolve(final int startRow, final int startColumn, final boolean inclusive) {
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


    private boolean hasNoConflicts(final Cell cell) {
        return getConflictingCells(cell.row, cell.column, false).isEmpty();
    }

    protected abstract Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll);

    protected final Set<Cell> getConflictingCellsInSameRowOrColumn(final int row, final int column,
                                                                   final boolean getAll) {
        final Set<Cell> conflicts = getAll ? new HashSet<>() : new HashSet<>(1);

        // check for appearance of grid[row][column] in same row/column
        for (int index = 0; index < gridSize; index++) {
            if (row != index && grid[row][column] == grid[index][column]) { // grid[row][column] twice in row
                conflicts.add(new Cell(index, column));
                if (!getAll) {
                    return conflicts;
                }
            }
            if ((column != index && grid[row][column] == grid[row][index])) { // grid[row][column] twice in column
                conflicts.add(new Cell(row, index));
                if (!getAll) {
                    return conflicts;
                }
            }
        }
        return conflicts;
    }


    protected boolean isInvalid() {
        if (grid.length != gridSize) {
            return true; // wrong amount of rows
        }

        for (int row = 0; row < gridSize; row++) {
            if (grid[row].length != gridSize) {
                return true; // wrong amount of columns
            }

            for (int column = 0; column < gridSize; column++) {
                final int currentCell = grid[row][column];

                if (currentCell == EMPTY_CELL) {
                    continue; // empty cell is always ok
                }

                if (currentCell < 1 || currentCell > gridSize || !getConflictingCells(row, column, false).isEmpty()) {
                    return true;
                }
            }
        }
        return false; // no rule was violated -> valid
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BasePuzzle basePuzzle = (BasePuzzle) o;
        return gridSize == basePuzzle.gridSize && deepEquals(grid, basePuzzle.grid);
    }

    @Override
    public int hashCode() {
        int result = hash(gridSize);
        result = 31 * result + deepHashCode(grid);
        return result;
    }
}
