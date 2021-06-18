package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.deepEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("Sudoku")
class SudokuTest {

    private Sudoku cut;

    // https://sudoku.tagesspiegel.de/files/2021/14062021_sudoku_hard.pdf
    private final int[][] grid = {
            {0, 0, 2,/**/ 0, 0, 7,/**/ 0, 9, 0},
            {0, 0, 0,/**/ 0, 0, 0,/**/ 6, 0, 0},
            {0, 0, 7,/**/ 0, 9, 3,/**/ 0, 0, 0},
//          /********/**/*********/**/********/
            {6, 8, 0,/**/ 0, 0, 2,/**/ 0, 7, 0},
            {0, 0, 0,/**/ 0, 3, 5,/**/ 0, 0, 0},
            {0, 3, 0,/**/ 0, 0, 9,/**/ 4, 0, 8},
//          /********/**/*********/**/********/
            {0, 7, 0,/**/ 1, 0, 0,/**/ 0, 0, 0},
            {9, 4, 0,/**/ 0, 2, 6,/**/ 0, 0, 0},
            {2, 0, 0,/**/ 0, 0, 0,/**/ 3, 0, 0},
    };

    // https://sudoku.tagesspiegel.de/files/2021/14062021_solve_hard.pdf
    private final int[][] solution = {
            {3, 6, 2,/**/ 5, 8, 7,/**/ 1, 9, 4},
            {5, 9, 8,/**/ 2, 4, 1,/**/ 6, 3, 7},
            {4, 1, 7,/**/ 6, 9, 3,/**/ 8, 5, 2},
//          /********/**/*********/**/********/
            {6, 8, 9,/**/ 4, 1, 2,/**/ 5, 7, 3},
            {7, 2, 4,/**/ 8, 3, 5,/**/ 9, 1, 6},
            {1, 3, 5,/**/ 7, 6, 9,/**/ 4, 2, 8},
//          /********/**/*********/**/********/
            {8, 7, 3,/**/ 1, 5, 4,/**/ 2, 6, 9},
            {9, 4, 1,/**/ 3, 2, 6,/**/ 7, 8, 5},
            {2, 5, 6,/**/ 9, 7, 8,/**/ 3, 4, 1},
    };


    @BeforeEach
    void createSudoku() {
        cut = new Sudoku(grid);
    }


    @Test
    @DisplayName("should be created with empty grid")
    void shouldBeCreatedWithEmptyGrid() {
        assertEquals(0, Sudoku.EMPTY_CELL);
        assertTrue(deepEquals(
                new int[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE],
                new Sudoku().getGrid()
        ));
    }

    @Test
    @DisplayName("should be created with grid equal to but not same as specified one")
    void shouldBeCreatedWithGridEqualToButNotSameAsSpecifiedOne() {
        assertNotSame(grid, cut.getGrid());
        assertTrue(deepEquals(
                grid,
                cut.getGrid()
        ));
    }

    @Test
    @DisplayName("should not be created with invalid grid")
    void shouldNotBeCreatedWithInvalidGrid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Sudoku(new int[][]{{-1}, {1, 2}})
        );
    }

    @Test
    @DisplayName("should be solved")
    void shouldBeSolved() {
        assertTrue(cut.solve());
        assertTrue(deepEquals(
                solution,
                cut.getGrid()
        ));
    }

    @Test
    @DisplayName("should keep prefilled values after solve")
    void shouldKeepPrefilledValuesAfterSolve() {
        cut.solve();
        final int[][] solvedGrid = cut.getGrid();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {

                if (grid[row][column] != Sudoku.EMPTY_CELL) {
                    assertEquals(grid[row][column], solvedGrid[row][column]);
                }
            }
        }
    }

    @Test
    @DisplayName("should only have values between 1 and Sudoku.GRID_SIZE after successful solve")
    void shouldOnlyHaveValuesBetween1AndSudokuGridSizeAfterSuccessfulSolve() {
        assumeTrue(cut.solve());

        for (final int[] row : cut.getGrid()) {
            for (final int cell : row) {
                assertTrue(cell > 0);
                assertTrue(cell <= Sudoku.GRID_SIZE);
            }
        }
    }
}
