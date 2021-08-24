package model;

import java.util.*;

import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Collections.*;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * Abstract base class for all puzzles like {@link Sudoku}, {@link Killer} and {@link Str8ts} that have a square grid
 * with the constraints that each number has to be in the range from 1 to the number of rows/columns in the grid
 * (see {@link #getGridSize()}) and no duplicate values are allowed in a row/column.
 * <p>
 * This class provides the following public interface:
 * <ul>
 * <li>{@link #getGridSize()} and {@link #getNumberOfCells()} - for providing information about a puzzle</li>
 * <li>
 * {@link Cell Cell}, {@link #getCell(int, int) getCell()}, {@link #setCell(int, int, int) setCell()},
 * {@link #resetCell(int, int) resetCell()} and {@link SetCellResult SetCellResult} - for managing the cells of a puzzle
 * </li>
 * <li>{@link #solve()} and {@link SolveResult SolveResult} - for solving a puzzle</li>
 * <li>{@link #equals(Object) equals()} and {@link #hashCode()} - for comparing puzzles</li>
 * </ul>
 *
 * @author Luca Kellermann
 */
public abstract class AbstractPuzzle {

    /**
     * A cell in the grid of a Puzzle in the form of an ordered nominal Pair {@code (row, column)}.
     */
    public static final record Cell(int row, int column) {}

    /**
     * Result of {@link #setCell(int, int, int) setCell()}.
     *
     * @param isSuccess        whether the {@link #setCell(int, int, int) setCell()} operation was successful
     * @param conflictingCells a {@link Set} of {@link Cell cells} that are responsible for the
     *                         {@link #setCell(int, int, int) setCell()} operation to fail
     */
    public static final record SetCellResult(boolean isSuccess, Set<Cell> conflictingCells) {

        /**
         * Constant that {@link #setCell(int, int, int) setCell()} always returns on success and can therefore be
         * used for identity comparison.
         */
        public static final SetCellResult SUCCESS = new SetCellResult(true, emptySet());

        /**
         * Constant that {@link #setCell(int, int, int) setCell()} always returns when you pass an invalid value for
         * {@code value} and can therefore be used for identity comparison.
         */
        public static final SetCellResult INVALID_VALUE = new SetCellResult(false, emptySet());
    }

    /**
     * Result of {@link #solve()}.
     */
    public enum SolveResult {

        /**
         * The Puzzle is in a state that is not ready for being solved (e.g. missing groups in a {@link Killer}).
         */
        NOT_IN_VALID_STATE_FOR_SOLVE,

        /**
         * The Puzzle is not solvable.
         */
        NO_SOLUTION,

        /**
         * The Puzzle was solved, but there was more than one way to do so.
         */
        MULTIPLE_SOLUTIONS,

        /**
         * The Puzzle was solved and there was exactly one way to do so.
         */
        ONE_SOLUTION,
    }


    public static final int EMPTY_CELL = 0;


    protected final int[][] grid;
    protected final int gridSize;


    protected AbstractPuzzle(final int[][] grid) {
        this.grid = grid;
        this.gridSize = grid.length;
    }


    protected abstract AbstractPuzzle getCopy();


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


    /**
     * Returns the value of the cell in the specified {@code row} and {@code column}.
     */
    public final int getCell(final int row, final int column) {
        return grid[row][column];
    }

    /**
     * Sets the value of the cell in the specified {@code row} and {@code column} to {@link #EMPTY_CELL}.
     */
    public final void resetCell(final int row, final int column) {
        setCell(row, column, EMPTY_CELL);
    }

    /**
     * Trys to set the value of the cell in the specified {@code row} and {@code column} to {@code value}.
     * <p>This will not work if {@code value} leads to conflicts with other cells or you try to pass something for
     * {@code value} that is neither {@link #EMPTY_CELL} nor in the valid range from {@code 1} to
     * {@link #getGridSize() gridSize} (both inclusive).</p>
     *
     * @return {@link SetCellResult#SUCCESS SetCellResult.SUCCESS} if successful,
     * {@link SetCellResult#INVALID_VALUE SetCellResult.INVALID_VALUE} if you try to use a {@code value} that is out of
     * range or another instance of {@link SetCellResult} that contains the
     * {@link SetCellResult#conflictingCells() conflictingCells} that are responsible for the operation to fail
     */
    public final SetCellResult setCell(final int row, final int column, final int value) {

        // empty cell is always ok
        if (value == EMPTY_CELL) {
            grid[row][column] = EMPTY_CELL;
            return SetCellResult.SUCCESS;
        }

        // invalid value -> don't check further
        if (value < 1 || value > gridSize) {
            return SetCellResult.INVALID_VALUE;
        }

        final int previousCellValue = grid[row][column];
        grid[row][column] = value;

        final Set<Cell> conflictingCells = getConflictingCells(row, column, true);

        if (conflictingCells.isEmpty()) {
            return SetCellResult.SUCCESS;
        } else {
            grid[row][column] = previousCellValue; // undo setting invalid number
            return new SetCellResult(false, conflictingCells);
        }
    }


    /**
     * Trys to solve the Puzzle.
     * <p>If the solve was successful, the Puzzle will be in a solved state, otherwise the sate will be the same as
     * before.</p>
     *
     * @return {@link SolveResult#NOT_IN_VALID_STATE_FOR_SOLVE SolveResult.NOT_IN_VALID_STATE_FOR_SOLVE} if this Puzzle
     * is in a state that is not ready for being solved (e.g. missing groups in a {@link Killer}),
     * {@link SolveResult#NO_SOLUTION SolveResult.NO_SOLUTION} if this Puzzle is not solvable,
     * {@link SolveResult#MULTIPLE_SOLUTIONS SolveResult.MULTIPLE_SOLUTIONS} if this Puzzle was solved but there was
     * more than one way to do so or {@link SolveResult#ONE_SOLUTION SolveResult.ONE_SOLUTION} if this Puzzle was solved
     * and there was exactly one way to do so
     */
    public final SolveResult solve() {

        if (hasToValidateBeforeSolve() && isInvalid()) {
            return SolveResult.NOT_IN_VALID_STATE_FOR_SOLVE;
        }

        final AbstractPuzzle copy = getCopy();

        if (!solveInNormalOrder()) {
            return SolveResult.NO_SOLUTION;
        }

        if (!copy.solveInReverseOrder()) {
            throw new IllegalStateException("Unable to solve copy while original could be solved");
        }

        return this.equals(copy) ? SolveResult.ONE_SOLUTION : SolveResult.MULTIPLE_SOLUTIONS;
    }

    // package-private for tests
    final boolean solveInNormalOrder() {
        return solveInternal(getNextEmptyCellForSolve(0, 0, true), getNumbersForSolve(false), null);
    }

    // package-private for tests
    final boolean solveInReverseOrder() {
        return solveInternal(getNextEmptyCellForSolve(0, 0, true), getNumbersForSolve(true), null);
    }

    // package-private for tests
    final boolean solveInRandomOrder(final Random random) {
        return solveInternal(getNextEmptyCellForSolve(0, 0, true), getNumbersForSolve(false), requireNonNull(random));
    }

    private ArrayList<Integer> getNumbersForSolve(final boolean inReverseOrder) {
        // get all numbers that are an option for filling a cell
        final ArrayList<Integer> numbers = new ArrayList<>(gridSize);
        if (inReverseOrder) {
            for (int number = gridSize; number >= 1; number--) {
                numbers.add(number);
            }
        } else {
            for (int number = 1; number <= gridSize; number++) {
                numbers.add(number);
            }
        }
        return numbers;
    }

    private boolean solveInternal(final Cell currentCell, final ArrayList<Integer> numbers, final Random random) {

        if (currentCell == null) {
            return true; // all cells are filled (only valid fills happen) -> found solution
        }

        final int currentRow = currentCell.row, currentColumn = currentCell.column;

        final Cell nextEmptyCell = getNextEmptyCellForSolve(currentRow, currentColumn, false);

        // shuffle when solving in random order
        if (random != null) {
            shuffle(numbers, random);
        }

        for (final int number : numbers) {
            grid[currentRow][currentColumn] = number; // choose next number

            // no conflicts && recursive solve successful -> found solution
            // copy numbers when solving in random order (shuffle in recursion would otherwise impact iteration here)
            if (getConflictingCells(currentRow, currentColumn, false).isEmpty()
                    && solveInternal(nextEmptyCell, random == null ? numbers : new ArrayList<>(numbers), random)) {
                return true;
            }
        }

        // no number was valid -> undo and go back in recursion
        grid[currentRow][currentColumn] = EMPTY_CELL;
        return false;
    }


    protected Cell getNextEmptyCellForSolve(final int startRow, final int startColumn, final boolean inclusive) {

        boolean lookingAtFirstCell = true;

        // start row with startRow
        for (int row = startRow; row < gridSize; row++) {

            // start column with startColumn (but only in first iteration of outer loop)
            for (int column = (row == startRow ? startColumn : 0); column < gridSize; column++) {

                // skip the first cell if not inclusive
                if (lookingAtFirstCell) {
                    lookingAtFirstCell = false;
                    if (!inclusive) continue;
                }

                if (grid[row][column] == EMPTY_CELL) {
                    return new Cell(row, column);
                }
            }
        }

        return null; // reached end and did not find an empty cell
    }


    protected Set<Cell> getConflictingCells(final int row, final int column, final boolean getAll) {

        final Set<Cell> conflicts = getAll ? new HashSet<>() : new HashSet<>(1); // only need 1 when !getAll

        // check for appearance of grid[row][column] in same row/column
        for (int index = 0; index < gridSize; index++) {
            // grid[row][column] twice in row
            if (row != index && grid[row][column] == grid[index][column]) {
                conflicts.add(new Cell(index, column));
                if (!getAll) {
                    return conflicts;
                }
            }
            // grid[row][column] twice in column
            if ((column != index && grid[row][column] == grid[row][index])) {
                conflicts.add(new Cell(row, index));
                if (!getAll) {
                    return conflicts;
                }
            }
        }

        return conflicts;
    }


    /**
     * Used to determine if there are requirements that need to be fulfilled before solving can be done.
     * If this returns {@code true}, the requirements are then checked by {@link #isInvalid()}.
     * Implementations should just return a constant value.
     */
    protected abstract boolean hasToValidateBeforeSolve();

    /**
     * Returns {@code false} if and only if {@link #grid} is a square with {@link #gridSize} {@code *} {@link #gridSize}
     * cells, every cell is either {@link #EMPTY_CELL} or in the valid range from {@code 1} to {@link #gridSize}
     * (both inclusive) and there are no {@link #getConflictingCells(int, int, boolean) conflicts}.
     */
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
    public final boolean equals(final Object obj) {
        if (obj == this) return true;

        // make sure that it's the same class -> isEqualTo() can safely cast to its own class
        if (obj == null || getClass() != obj.getClass()) return false;

        final AbstractPuzzle other = (AbstractPuzzle) obj;
        return this.gridSize == other.gridSize && deepEquals(this.grid, other.grid) && this.isEqualTo(other);
    }

    protected abstract boolean isEqualTo(final AbstractPuzzle other);

    @Override
    public int hashCode() {
        int result = hash(gridSize);
        result = 31 * result + deepHashCode(grid);
        return result;
    }
}
