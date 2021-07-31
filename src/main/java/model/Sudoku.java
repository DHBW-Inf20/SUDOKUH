package model;

import java.util.Random;

import static java.util.Arrays.deepToString;

public final class Sudoku extends AbstractSudoku {

    public static final int DEFAULT_SUB_GRID_SIZE = 3;
    public static final int DEFAULT_GRID_SIZE = DEFAULT_SUB_GRID_SIZE * DEFAULT_SUB_GRID_SIZE;


    public Sudoku() {
        super(DEFAULT_SUB_GRID_SIZE);
    }

    public Sudoku(final int subGridSize) {
        super(subGridSize);
    }

    public Sudoku(final int subGridSize, final Random random) {
        super(subGridSize);
        if (!solveInRandomOrder(random)) {
            throw new IllegalStateException("Could not solve empty sudoku, something is wrong!");
        }
    }

    // package-private for tests
    Sudoku(final int[][] grid) {
        super(grid);
        if (isInvalid()) {
            throw new IllegalArgumentException("Input sudoku is not valid!");
        }
    }


    @Override
    public Sudoku getCopy() {
        return new Sudoku(grid);
    }


    @Override
    protected boolean hasToValidateBeforeSolve() {
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
