package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.deepEquals;
import static model.Util.allCellRowsAndColumnsForGridWithSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author Luca Kellermann
 */
@DisplayName("Sudoku")
class SudokuTest {

    private Sudoku sudoku;
    private Sudoku solution;

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
    private static final int[][] solutionGrid = {
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


    static List<Arguments> allCellRowsAndColumnsForGrid() {
        return allCellRowsAndColumnsForGridWithSize(grid.length);
    }


    @BeforeEach
    void createSudokuAndSolution() {
        sudoku = new Sudoku(grid);
        solution = new Sudoku(solutionGrid);
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
    @DisplayName("should not set cell with invalid number")
    void shouldNotSetCellWithInvalidNumber() {
        Sudoku.SetCellResult result;

        result = sudoku.setCell(0, 0, sudoku.getGridSize() + 1); // number too big
        assertSame(Sudoku.SetCellResult.INVALID_VALUE, result);
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getCell(0, 0));

        result = sudoku.setCell(0, 0, 2); // 2 is already in same row, see grid
        assertFalse(result.isSuccess());
        assertTrue(result.conflictingCells().contains(new Sudoku.Cell(0, 2)));
        assertTrue(result.conflictingCells().contains(new Sudoku.Cell(8, 0)));
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getCell(0, 0));

        result = sudoku.setCell(0, 0, Sudoku.EMPTY_CELL - 1); // number too small
        assertSame(Sudoku.SetCellResult.INVALID_VALUE, result);
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getCell(0, 0));
    }

    @Test
    @DisplayName("should set cell with valid number")
    void shouldSetCellWithValidNumber() {
        assertSame(Sudoku.SetCellResult.SUCCESS, sudoku.setCell(0, 0, 3)); // 3 is valid, see solution
        assertEquals(3, sudoku.getCell(0, 0));
    }

    @ParameterizedTest(name = "should set cell ({0},{1}) with Sudoku.EMPTY_CELL")
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should set any cell with Sudoku.EMPTY_CELL")
    void shouldSetAnyCellWithSudokuEmptyCell(final int row, final int column) {
        assertSame(Sudoku.SetCellResult.SUCCESS, sudoku.setCell(row, column, Sudoku.EMPTY_CELL));
        assertEquals(Sudoku.EMPTY_CELL, sudoku.getCell(row, column));

        assertSame(Sudoku.SetCellResult.SUCCESS, solution.setCell(row, column, Sudoku.EMPTY_CELL));
        assertEquals(Sudoku.EMPTY_CELL, solution.getCell(row, column));
    }

    @Test
    @DisplayName("should be solved")
    void shouldBeSolved() {
        assertTrue(sudoku.solveInNormalOrder());
        assertEquals(solution, sudoku);
    }

    @Test
    @DisplayName("should be solved in reverse order")
    void shouldBeSolvedInReverseOrder() {
        assertTrue(sudoku.solveInReverseOrder());
        assertEquals(solution, sudoku);
    }

    @Test
    @DisplayName("should be solved in random order")
    void shouldBeSolvedInRandomOrder() {
        assertTrue(sudoku.solveInRandomOrder(new Random()));
        assertEquals(solution, sudoku);
    }

    @ParameterizedTest(name = "cell ({0},{1}) should keep prefilled value after solve")
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("any cell should keep prefilled value after solve")
    void anyCellShouldKeepPrefilledValueAfterSolve(final int row, final int column) {
        sudoku.solveInNormalOrder();

        if (grid[row][column] != Sudoku.EMPTY_CELL) {
            assertEquals(grid[row][column], sudoku.getCell(row, column));
        }
    }

    @ParameterizedTest(name = "cell ({0},{1}) should have value between 1 and getGridSize() after successful solve")
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("any cell should have value between 1 and getGridSize() after successful solve")
    void anyCellShouldHaveValueBetween1AndGetGridSizeAfterSuccessfulSolve(final int row, final int column) {
        assumeTrue(sudoku.solveInNormalOrder());

        final int cell = sudoku.getCell(row, column);

        assertTrue(cell > 0);
        assertTrue(cell <= sudoku.getGridSize());
    }
}
