package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Collections.reverse;
import static java.util.Collections.shuffle;
import static java.util.Objects.hash;

public abstract class BasePuzzle {

    public static final int EMPTY_CELL = 0;


    protected static record Cell(int row, int column) {}

    public static record SetResult(boolean isSuccess, int conflictingRow, int conflictingColumn) {
        public static SetResult Success = new SetResult(true, -1, -1);
        public static SetResult InvalidValue = new SetResult(false, -1, -1);
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
            return SetResult.Success;
        }

        // invalid value -> don't check further
        if (value < 1 || value > gridSize) {
            return SetResult.InvalidValue;
        }

        final int previousCellValue = grid[row][column];
        grid[row][column] = value;
        final Cell conflictingCell = conflictingCell(new Cell(row, column));
        if (conflictingCell == null) {
            return SetResult.Success;
        } else {
            grid[row][column] = previousCellValue; // undo setting invalid number
            return new SetResult(false, conflictingCell.row, conflictingCell.column);
        }
    }


    public final boolean solve() {
        return solveInternal(getNextEmptyCellForSolve(new Cell(0, 0), true), false, null);
    }

    public final boolean solveInReverseOrder() {
        return solveInternal(getNextEmptyCellForSolve(new Cell(0, 0), true), true, null);
    }

    // package-private for tests
    final boolean solveInRandomOrder(final Random random) {
        return solveInternal(getNextEmptyCellForSolve(new Cell(0, 0), true), false, random);
    }

    private boolean solveInternal(final Cell currentCell, final boolean inReverseOrder, final Random random) {

        if (currentCell == null) {
            return true; // all cells are filled (only valid fills happen) -> found solution
        }

        // start with next cell (current is empty) -> call with inclusive = false
        final Cell nextEmptyCell = getNextEmptyCellForSolve(currentCell, false);

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

            if (conflictingCell(currentCell) == null &&                      // number was valid and
                    solveInternal(nextEmptyCell, inReverseOrder, random)) {  // recursive solve was successful
                return true;                                                 // -> found solution
            }
        }

        grid[currentCell.row][currentCell.column] = EMPTY_CELL; // no number was valid -> undo and go back in recursion
        return false;
    }


    protected abstract Cell getNextEmptyCellForSolve(final Cell startCell, final boolean inclusive);

    protected abstract Cell conflictingCell(final Cell cell);


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
