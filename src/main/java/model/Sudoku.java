package model;

import java.util.Random;

import static java.util.Arrays.deepToString;

public final class Sudoku extends AbstractSudoku {

    public static final int DEFAULT_SUB_GRID_SIZE = 3;
    public static final int DEFAULT_GRID_SIZE = DEFAULT_SUB_GRID_SIZE * DEFAULT_SUB_GRID_SIZE;


    /**
     * Creates an empty Sudoku with {@link #DEFAULT_SUB_GRID_SIZE} for its {@link #getSubGridSize() subGridSize} and
     * {@link #DEFAULT_GRID_SIZE} for its {@link #getGridSize() gridSize}.
     */
    public Sudoku() {
        this(DEFAULT_SUB_GRID_SIZE);
    }

    /**
     * Creates an empty Sudoku with the specified {@link #getSubGridSize() subGridSize}.
     * Its {@link #getGridSize() gridSize} is the square of {@code subGridSize}.
     */
    public Sudoku(final int subGridSize) {
        super(subGridSize);
    }

    /**
     * Creates Sudoku with the specified {@link #getSubGridSize() subGridSize} filled with random values in a valid way.
     * Its {@link #getGridSize() gridSize} is the square of {@code subGridSize}.
     *
     * @param random the {@link Random} instance used for filling the Sudoku randomly
     * @throws NullPointerException if {@code random} is {@code null}
     */
    public Sudoku(final int subGridSize, final Random random) {
        this(subGridSize);
        if (!solveInRandomOrder(random)) { // fill with random values
            throw new IllegalStateException("Could not solve empty sudoku, something is wrong!");
        }
    }

    // package-private constructor for tests
    Sudoku(final int[][] grid) {
        super(grid);
        if (isInvalid()) {
            throw new IllegalArgumentException("Input sudoku is not valid!");
        }
    }


    /**
     * Returns a copy of this Sudoku.
     */
    @Override
    public Sudoku getCopy() {
        return new Sudoku(grid);
    }


    @Override
    protected boolean hasToValidateBeforeSolve() {
        // there are no requirements for the structure that MUST be fulfilled in order to solve a Sudoku
        // (could e.g. also solve an empty Sudoku)
        return false;
    }


    @Override
    public String toString() {
        return "Sudoku{" +
                "subGridSize=" + subGridSize +
                ", gridSize=" + gridSize +
                ", grid=" + deepToString(grid) +
                '}';
    }
}
