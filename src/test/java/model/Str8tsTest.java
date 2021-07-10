package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.deepEquals;
import static java.util.Arrays.fill;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Str8ts")
public class Str8tsTest {
    private static final Str8ts.Color B = Str8ts.Color.BLACK;
    private static final Str8ts.Color W = Str8ts.Color.WHITE;

// -------------------------------- gentle --------------------------------
    // https://de.wikipedia.org/wiki/Datei:Str8ts9x9_Gentle_PUZ.png
    // https://de.wikipedia.org/wiki/Datei:Str8ts9x9_Gentle_SOL.png

    private Str8ts gentleStr8ts;
    private Str8ts gentleSolution;

    private static final int[][] gentleGrid = {
            {0, 0, 0,/**/ 0, 5, 0,/**/ 0, 3, 0},
            {0, 6, 0,/**/ 0, 0, 0,/**/ 1, 0, 0},
            {0, 0, 0,/**/ 0, 8, 0,/**/ 0, 0, 0},
//          /********/**/*********/**/********/
            {9, 0, 0,/**/ 4, 0, 0,/**/ 0, 0, 5},
            {0, 0, 0,/**/ 0, 0, 3,/**/ 0, 0, 0},
            {0, 0, 0,/**/ 0, 0, 9,/**/ 0, 4, 0},
//          /********/**/*********/**/********/
            {4, 0, 3,/**/ 0, 0, 0,/**/ 0, 6, 0},
            {0, 0, 1,/**/ 0, 0, 0,/**/ 0, 0, 0},
            {0, 0, 8,/**/ 0, 0, 0,/**/ 0, 0, 2},
    };

    private static final int[][] gentleSolutionGrid = {
            {0, 0, 9,/**/ 6, 5, 8,/**/ 7, 3, 0},
            {8, 6, 5,/**/ 7, 0, 0,/**/ 1, 2, 3},
            {7, 5, 6,/**/ 0, 8, 2,/**/ 3, 1, 4},
//          /********/**/*********/**/********/
            {9, 8, 7,/**/ 4, 3, 1,/**/ 2, 0, 5},
            {0, 7, 4,/**/ 1, 2, 3,/**/ 6, 5, 0},
            {0, 0, 2,/**/ 3, 1, 9,/**/ 5, 4, 6},
//          /********/**/*********/**/********/
            {4, 1, 3,/**/ 2, 0, 0,/**/ 8, 6, 7},
            {3, 2, 1,/**/ 0, 0, 6,/**/ 9, 7, 8},
            {0, 0, 8,/**/ 5, 6, 7,/**/ 4, 0, 2},
    };

    private static final Str8ts.Color[][] gentleColors = {
            {B, B, W,/**/ W, W, W,/**/ W, B, B},
            {W, W, W,/**/ W, B, B,/**/ W, W, W},
            {W, W, W,/**/ B, B, W,/**/ W, W, W},
//          /********/**/*********/**/********/
            {W, W, W,/**/ B, W, W,/**/ W, B, B},
            {B, W, W,/**/ W, W, W,/**/ W, W, B},
            {B, B, W,/**/ W, W, B,/**/ W, W, W},
//          /********/**/*********/**/********/
            {W, W, W,/**/ W, B, B,/**/ W, W, W},
            {W, W, W,/**/ B, B, W,/**/ W, W, W},
            {B, B, W,/**/ W, W, W,/**/ W, B, B},
    };

// -------------------------------- very hard --------------------------------
    // https://de.wikipedia.org/wiki/Datei:Str8ts9x9_Very_Hard_PUZ.png
    // https://de.wikipedia.org/wiki/Datei:Str8ts9x9_Very_Hard_SOL.png

    private Str8ts veryHardStr8ts;
    private Str8ts veryHardSolution;

    private static final int[][] veryHardGrid = {
            {0, 0, 2,/**/ 0, 0, 0,/**/ 9, 0, 0},
            {0, 0, 0,/**/ 0, 0, 8,/**/ 6, 0, 0},
            {5, 2, 0,/**/ 0, 6, 0,/**/ 0, 0, 0},
//          /********/**/*********/**/********/
            {0, 0, 0,/**/ 0, 0, 0,/**/ 0, 0, 0},
            {0, 0, 1,/**/ 5, 0, 0,/**/ 0, 0, 0},
            {0, 0, 0,/**/ 0, 0, 0,/**/ 0, 0, 6},
//          /********/**/*********/**/********/
            {0, 0, 0,/**/ 0, 5, 0,/**/ 0, 3, 0},
            {7, 0, 0,/**/ 9, 0, 0,/**/ 0, 0, 0},
            {0, 0, 0,/**/ 0, 0, 2,/**/ 0, 4, 0},
    };

    private static final int[][] veryHardSolutionGrid = {
            {0, 0, 2,/**/ 4, 3, 0,/**/ 9, 8, 7},
            {0, 3, 5,/**/ 2, 4, 8,/**/ 6, 7, 0},
            {5, 2, 4,/**/ 3, 6, 7,/**/ 8, 9, 0},
//          /********/**/*********/**/********/
            {2, 1, 3,/**/ 8, 9, 4,/**/ 7, 6, 5},
            {4, 0, 1,/**/ 5, 7, 6,/**/ 0, 0, 3},
            {3, 4, 9,/**/ 7, 8, 5,/**/ 1, 2, 6},
//          /********/**/*********/**/********/
            {0, 7, 8,/**/ 6, 5, 1,/**/ 2, 3, 4},
            {7, 5, 6,/**/ 9, 2, 3,/**/ 4, 1, 0},
            {8, 6, 7,/**/ 0, 1, 2,/**/ 3, 4, 0},
    };

    private static final Str8ts.Color[][] veryHardColors = {
            {B, B, W,/**/ W, W, B,/**/ W, W, W},
            {B, W, W,/**/ W, W, B,/**/ W, W, B},
            {W, W, W,/**/ W, B, W,/**/ W, W, B},
//          /********/**/*********/**/********/
            {W, W, W,/**/ W, W, W,/**/ W, W, W},
            {W, B, B,/**/ W, W, W,/**/ B, B, W},
            {W, W, W,/**/ W, W, W,/**/ W, W, W},
//          /********/**/*********/**/********/
            {B, W, W,/**/ W, B, W,/**/ W, W, W},
            {B, W, W,/**/ B, W, W,/**/ W, W, B},
            {W, W, W,/**/ B, W, W,/**/ W, B, B},
    };


    @SuppressWarnings("unused")
    static List<Arguments> allCellRowsAndColumnsForGrid() {
        final int size = gentleGrid.length;
        List<Arguments> list = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                list.add(Arguments.of(row, column));
            }
        }
        return list;
    }


    @BeforeEach
    void createStr8tsAndSolutions() {
        gentleStr8ts = new Str8ts(gentleGrid, gentleColors);
        gentleSolution = new Str8ts(gentleSolutionGrid, gentleColors);

        veryHardStr8ts = new Str8ts(veryHardGrid, veryHardColors);
        veryHardSolution = new Str8ts(veryHardSolutionGrid, veryHardColors);
    }


    @Test
    @DisplayName("should be created with empty grid")
    void shouldBeCreatedWithEmptyGrid() {
        assertEquals(0, Str8ts.EMPTY_CELL);
        assertTrue(deepEquals(
                new int[Str8ts.GRID_SIZE][Str8ts.GRID_SIZE],
                new Str8ts().getGrid()
        ));
    }

    @Test
    @DisplayName("should be created with white colors")
    void shouldBeCreatedWithWhiteColors() {
        @SuppressWarnings("MismatchedReadAndWriteOfArray") final Str8ts.Color[][] colors = new Str8ts.Color[Str8ts.GRID_SIZE][Str8ts.GRID_SIZE];
        for (Str8ts.Color[] row : colors) {
            fill(row, Str8ts.Color.WHITE);
        }
        assertTrue(deepEquals(
                colors,
                new Str8ts().getColors()
        ));
    }

    @Test
    @DisplayName("should be created with grid equal to but not same as specified one")
    void shouldBeCreatedWithGridEqualToButNotSameAsSpecifiedOne() {
        assertNotSame(gentleGrid, gentleStr8ts.getGrid());
        assertTrue(deepEquals(
                gentleGrid,
                gentleStr8ts.getGrid()
        ));
        assertNotSame(gentleSolutionGrid, gentleSolution.getGrid());
        assertTrue(deepEquals(
                gentleSolutionGrid,
                gentleSolution.getGrid()
        ));
        assertNotSame(veryHardGrid, veryHardStr8ts.getGrid());
        assertTrue(deepEquals(
                veryHardGrid,
                veryHardStr8ts.getGrid()
        ));
        assertNotSame(veryHardSolutionGrid, veryHardSolution.getGrid());
        assertTrue(deepEquals(
                veryHardSolutionGrid,
                veryHardSolution.getGrid()
        ));
    }

    @Test
    @DisplayName("should not be created with invalid grid and colors")
    void shouldNotBeCreatedWithInvalidGridAndColors() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Str8ts(new int[][]{{-1}, {1, 2}},
                        new Str8ts.Color[][]{{Str8ts.Color.BLACK}, {Str8ts.Color.WHITE, null}})
        );
    }

    @Test
    @DisplayName("should not set cell with invalid number")
    void shouldNotSetCellWithInvalidNumber() {
        Str8ts.SetResult result;

        result = gentleStr8ts.setCell(0, 0, gentleStr8ts.getGridSize() + 1); // number too big
        assertSame(Str8ts.SetResult.INVALID_VALUE, result);
        assertEquals(Str8ts.EMPTY_CELL, gentleStr8ts.getCell(0, 0));

        result = gentleStr8ts.setCell(0, 0, 5); // 5 is already in same row, see gentleGrid
        assertFalse(result.isSuccess());
        assertTrue(result.conflictingCells().contains(new Str8ts.Cell(0, 4)));
        assertEquals(Str8ts.EMPTY_CELL, gentleStr8ts.getCell(0, 0));

        result = gentleStr8ts.setCell(0, 0, Str8ts.EMPTY_CELL - 1); // number too small
        assertSame(Str8ts.SetResult.INVALID_VALUE, result);
        assertEquals(Str8ts.EMPTY_CELL, gentleStr8ts.getCell(0, 0));
    }

    @Test
    @DisplayName("should set cell with valid number")
    void shouldSetCellWithValidNumber() {
        assertSame(Str8ts.SetResult.SUCCESS, gentleStr8ts.setCell(0, 2, 9)); // 9 is valid, see solution
        assertEquals(9, gentleStr8ts.getCell(0, 2));
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should set any cell with Str8ts.EMPTY_CELL")
    void shouldSetAnyCellWithStr8tsEmptyCell(final int row, final int column) {
        assertSame(Str8ts.SetResult.SUCCESS, gentleStr8ts.setCell(row, column, Str8ts.EMPTY_CELL));
        assertEquals(Str8ts.EMPTY_CELL, gentleStr8ts.getCell(row, column));

        assertSame(Str8ts.SetResult.SUCCESS, gentleSolution.setCell(row, column, Str8ts.EMPTY_CELL));
        assertEquals(Str8ts.EMPTY_CELL, gentleSolution.getCell(row, column));

        assertSame(Str8ts.SetResult.SUCCESS, veryHardStr8ts.setCell(row, column, Str8ts.EMPTY_CELL));
        assertEquals(Str8ts.EMPTY_CELL, veryHardStr8ts.getCell(row, column));

        assertSame(Str8ts.SetResult.SUCCESS, veryHardSolution.setCell(row, column, Str8ts.EMPTY_CELL));
        assertEquals(Str8ts.EMPTY_CELL, veryHardSolution.getCell(row, column));
    }

    @Test
    @DisplayName("should not set color to null")
    void shouldNotSetColorToNull() {
        assertThrows(
                NullPointerException.class,
                () -> gentleStr8ts.setColor(0, 0, null)
        );
    }

    @Test
    @DisplayName("should not set invalid color")
    void shouldNotSetInvalidColor() {
        // set this black cell to 9 is valid
        assertSame(Str8ts.SetResult.SUCCESS, gentleSolution.setCell(8, 1, 9));
        // but setting it to black causes the straight condition to be violated
        assertFalse(gentleSolution.setColor(8, 1, Str8ts.Color.WHITE));
        assertSame(Str8ts.Color.BLACK, gentleSolution.getColor(8, 1));
    }

    @Test
    @DisplayName("should set valid color")
    void shouldSetValidColor() {
        assertTrue(gentleStr8ts.setColor(3, 3, Str8ts.Color.WHITE));
        assertSame(Str8ts.Color.WHITE, gentleStr8ts.getColor(3, 3));
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should always set color to Str8ts.Color.BLACK")
    void shouldAlwaysSetColorToStr8tsColorBlack(final int row, final int column) {
        assertTrue(gentleStr8ts.setColor(row, column, Str8ts.Color.BLACK));
        assertSame(Str8ts.Color.BLACK, gentleStr8ts.getColor(row, column));

        assertTrue(gentleSolution.setColor(row, column, Str8ts.Color.BLACK));
        assertSame(Str8ts.Color.BLACK, gentleSolution.getColor(row, column));

        assertTrue(veryHardStr8ts.setColor(row, column, Str8ts.Color.BLACK));
        assertSame(Str8ts.Color.BLACK, veryHardStr8ts.getColor(row, column));

        assertTrue(veryHardSolution.setColor(row, column, Str8ts.Color.BLACK));
        assertSame(Str8ts.Color.BLACK, veryHardSolution.getColor(row, column));
    }

    @Test
    @DisplayName("should be solved")
    void shouldBeSolved() {
        assertTrue(gentleStr8ts.solveInNormalOrder());
        assertEquals(gentleSolution, gentleStr8ts);

        assertTrue(veryHardStr8ts.solveInNormalOrder());
        assertEquals(veryHardSolution, veryHardStr8ts);
    }

    @Test
    @DisplayName("should be solved in reverse order")
    void shouldBeSolvedInReverseOrder() {
        assertTrue(gentleStr8ts.solveInReverseOrder());
        assertEquals(gentleSolution, gentleStr8ts);

        assertTrue(veryHardStr8ts.solveInReverseOrder());
        assertEquals(veryHardSolution, veryHardStr8ts);
    }

    @Test
    @DisplayName("should be solved in random order")
    void shouldBeSolvedInRandomOrder() {
        final Random random = new Random();

        assertTrue(gentleStr8ts.solveInRandomOrder(random));
        assertEquals(gentleSolution, gentleStr8ts);

        assertTrue(veryHardStr8ts.solveInRandomOrder(random));
        assertEquals(veryHardSolution, veryHardStr8ts);
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should keep prefilled values after solve")
    void shouldKeepPrefilledValuesAfterSolve(final int row, final int column) {
        gentleStr8ts.solveInNormalOrder();
        if (gentleGrid[row][column] != Str8ts.EMPTY_CELL) {
            assertEquals(gentleGrid[row][column], gentleStr8ts.getCell(row, column));
        }

        veryHardStr8ts.solveInNormalOrder();
        if (veryHardGrid[row][column] != Str8ts.EMPTY_CELL) {
            assertEquals(veryHardGrid[row][column], veryHardStr8ts.getCell(row, column));
        }
    }
}
