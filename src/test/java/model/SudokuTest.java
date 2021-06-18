package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.deepEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("Sudoku")
class SudokuTest {

    private Sudoku sudoku;

    // https://sudoku.tagesspiegel.de/files/2021/14062021_sudoku_hard.pdf
    private static final int[][] grid = {
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
    private static final int[][] solution = {
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


    @SuppressWarnings("unused")
    static List<Arguments> allCellRowsAndColumnsForGrid() {
        final int size = grid.length;
        List<Arguments> list = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                list.add(Arguments.of(row, column));
            }
        }
        return list;
    }


    @BeforeEach
    void createSudoku() {
        sudoku = new Sudoku(grid);
    }


    @Test
    @DisplayName("should be created with empty grid")
    void shouldBeCreatedWithEmptyGrid() {
        assertEquals(0, Sudoku.EMPTY_CELL);
        assertTrue(deepEquals(
                new int[Sudoku.DEFAULT_GRID_SIZE][Sudoku.DEFAULT_GRID_SIZE],
                new Sudoku().getGrid()
        ));
    }

    @Test
    @DisplayName("should be created with grid equal to but not same as specified one")
    void shouldBeCreatedWithGridEqualToButNotSameAsSpecifiedOne() {
        assertNotSame(grid, sudoku.getGrid());
        assertTrue(deepEquals(
                grid,
                sudoku.getGrid()
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
    @DisplayName("should not set cell with invalid number")
    void shouldNotSetCellWithInvalidNumber() {
        assertFalse(sudoku.setCell(0, 0, sudoku.getGridSize() + 1)); // number too big
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getGrid()[0][0]);

        assertFalse(sudoku.setCell(0, 0, 2)); // 2 is already in same row, see grid
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getGrid()[0][0]);

        assertFalse(sudoku.setCell(0, 0, Sudoku.EMPTY_CELL - 1)); // number too small
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getGrid()[0][0]);
    }

    @Test
    @DisplayName("should set cell with valid number")
    void shouldSetCellWithValidNumber() {
        assertTrue(sudoku.setCell(0, 0, 3)); // 3 is valid, see solution
        assertEquals(3, sudoku.getGrid()[0][0]);
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should set any cell with Sudoku.EMPTY_CELL")
    void shouldSetAnyCellWithSudokuEmptyCell(final int row, final int column) {
        assertTrue(sudoku.setCell(row, column, Sudoku.EMPTY_CELL));
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getGrid()[row][column]);
    }

    @Test
    @DisplayName("should be solved")
    void shouldBeSolved() {
        assertTrue(sudoku.solve());
        assertTrue(deepEquals(
                solution,
                sudoku.getGrid()
        ));
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should keep prefilled values after solve")
    void shouldKeepPrefilledValuesAfterSolve(final int row, final int column) {
        sudoku.solve();
        final int[][] solvedGrid = sudoku.getGrid();

        if (grid[row][column] != Sudoku.EMPTY_CELL) {
            assertEquals(grid[row][column], solvedGrid[row][column]);
        }
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should only have values between 1 and Sudoku.GRID_SIZE after successful solve")
    void shouldOnlyHaveValuesBetween1AndSudokuGridSizeAfterSuccessfulSolve(final int row, final int column) {
        assumeTrue(sudoku.solve());

        final int cell = sudoku.getGrid()[row][column];

        assertTrue(cell > 0);
        assertTrue(cell <= sudoku.getGridSize());
    }
}
