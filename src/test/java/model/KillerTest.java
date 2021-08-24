package model;

import model.AbstractPuzzle.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.Arrays.deepEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author Luca Kellermann
 */
@DisplayName("Killer")
public class KillerTest {

    private Killer killer;
    private Killer solution;

    // https://de.wikipedia.org/wiki/Datei:Killerq.png
    private static final int[][] grid = new int[Killer.GRID_SIZE][Killer.GRID_SIZE]; // filled with 0

    // https://de.wikipedia.org/wiki/Datei:Killera.png
    private static final int[][] solutionGrid = {
            {2, 1, 5,/**/ 6, 4, 7,/**/ 3, 9, 8},
            {3, 6, 8,/**/ 9, 5, 2,/**/ 1, 7, 4},
            {7, 9, 4,/**/ 3, 8, 1,/**/ 6, 5, 2},
//          /********/**/*********/**/********/
            {5, 8, 6,/**/ 2, 7, 4,/**/ 9, 3, 1},
            {1, 4, 2,/**/ 5, 9, 3,/**/ 8, 6, 7},
            {9, 7, 3,/**/ 8, 1, 6,/**/ 4, 2, 5},
//          /********/**/*********/**/********/
            {8, 2, 1,/**/ 7, 3, 9,/**/ 5, 4, 6},
            {6, 5, 9,/**/ 4, 2, 8,/**/ 7, 1, 3},
            {4, 3, 7,/**/ 1, 6, 5,/**/ 2, 8, 9},
    };

    private static final List<Killer.Group> groups = List.of(
            new Killer.Group(Set.of(new Cell(0, 0), new Cell(0, 1)), 3),
            new Killer.Group(Set.of(new Cell(0, 2), new Cell(0, 3), new Cell(0, 4)), 15),
            new Killer.Group(Set.of(new Cell(0, 5), new Cell(1, 4), new Cell(1, 5), new Cell(2, 4)), 22),
            new Killer.Group(Set.of(new Cell(0, 6), new Cell(1, 6)), 4),
            new Killer.Group(Set.of(new Cell(0, 7), new Cell(1, 7)), 16),
            new Killer.Group(Set.of(new Cell(0, 8), new Cell(1, 8), new Cell(2, 8), new Cell(3, 8)), 15),
            new Killer.Group(Set.of(new Cell(1, 0), new Cell(1, 1), new Cell(2, 0), new Cell(2, 1)), 25),
            new Killer.Group(Set.of(new Cell(1, 2), new Cell(1, 3)), 17),
            new Killer.Group(Set.of(new Cell(2, 2), new Cell(2, 3), new Cell(3, 3)), 9),
            new Killer.Group(Set.of(new Cell(2, 5), new Cell(3, 5), new Cell(4, 5)), 8),
            new Killer.Group(Set.of(new Cell(2, 6), new Cell(2, 7), new Cell(3, 6)), 20),
            new Killer.Group(Set.of(new Cell(3, 0), new Cell(4, 0)), 6),
            new Killer.Group(Set.of(new Cell(3, 1), new Cell(3, 2)), 14),
            new Killer.Group(Set.of(new Cell(3, 4), new Cell(4, 4), new Cell(5, 4)), 17),
            new Killer.Group(Set.of(new Cell(3, 7), new Cell(4, 6), new Cell(4, 7)), 17),
            new Killer.Group(Set.of(new Cell(4, 1), new Cell(4, 2), new Cell(5, 1)), 13),
            new Killer.Group(Set.of(new Cell(4, 3), new Cell(5, 3), new Cell(6, 3)), 20),
            new Killer.Group(Set.of(new Cell(4, 8), new Cell(5, 8)), 12),
            new Killer.Group(Set.of(new Cell(5, 0), new Cell(6, 0), new Cell(7, 0), new Cell(8, 0)), 27),
            new Killer.Group(Set.of(new Cell(5, 2), new Cell(6, 1), new Cell(6, 2)), 6),
            new Killer.Group(Set.of(new Cell(5, 5), new Cell(6, 5), new Cell(6, 6)), 20),
            new Killer.Group(Set.of(new Cell(5, 6), new Cell(5, 7)), 6),
            new Killer.Group(Set.of(new Cell(6, 4), new Cell(7, 3), new Cell(7, 4), new Cell(8, 3)), 10),
            new Killer.Group(Set.of(new Cell(6, 7), new Cell(6, 8), new Cell(7, 7), new Cell(7, 8)), 14),
            new Killer.Group(Set.of(new Cell(7, 1), new Cell(8, 1)), 8),
            new Killer.Group(Set.of(new Cell(7, 2), new Cell(8, 2)), 16),
            new Killer.Group(Set.of(new Cell(7, 5), new Cell(7, 6)), 15),
            new Killer.Group(Set.of(new Cell(8, 4), new Cell(8, 5), new Cell(8, 6)), 13),
            new Killer.Group(Set.of(new Cell(8, 7), new Cell(8, 8)), 17)
    );


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
    void createKillerAndSolution() {
        killer = new Killer(grid, groups);
        solution = new Killer(solutionGrid, groups);
    }


    @Test
    @DisplayName("should be created with empty grid")
    void shouldBeCreatedWithEmptyGrid() {
        assertEquals(0, Killer.EMPTY_CELL);
        assertTrue(deepEquals(
                new int[Killer.GRID_SIZE][Killer.GRID_SIZE],
                new Killer().getGrid()
        ));
    }

    @Test
    @DisplayName("should not set cell with invalid number")
    void shouldNotSetCellWithInvalidNumber() {
        Killer.SetCellResult result;

        result = killer.setCell(0, 0, 4); // higher number than sum of group
        assertFalse(result.isSuccess());
        assertFalse(result.conflictingCells().isEmpty());
        assertEquals(Killer.EMPTY_CELL, killer.getCell(0, 0));

        result = killer.setCell(0, 0, killer.getGridSize() + 1); // number too big
        assertSame(Killer.SetCellResult.INVALID_VALUE, result);
        assertEquals(Killer.EMPTY_CELL, killer.getCell(0, 0));

        result = killer.setCell(0, 0, Killer.EMPTY_CELL - 1); // number too small
        assertSame(Killer.SetCellResult.INVALID_VALUE, result);
        assertEquals(Killer.EMPTY_CELL, killer.getCell(0, 0));
    }

    @Test
    @DisplayName("should set cell with valid number")
    void shouldSetCellWithValidNumber() {
        assertSame(Killer.SetCellResult.SUCCESS, killer.setCell(0, 0, 2)); // 2 is valid, see solution
        assertEquals(2, killer.getCell(0, 0));
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should set any cell with Killer.EMPTY_CELL")
    void shouldSetAnyCellWithKillerEmptyCell(final int row, final int column) {
        assertSame(Killer.SetCellResult.SUCCESS, solution.setCell(row, column, Killer.EMPTY_CELL));
        assertEquals(Killer.EMPTY_CELL, solution.getCell(row, column));
    }

    @Test
    @DisplayName("should be solved")
    void shouldBeSolved() {
        assertTrue(killer.solveInNormalOrder());
        assertEquals(solution, killer);
    }

    @Test
    @DisplayName("should be solved in reverse order")
    void shouldBeSolvedInReverseOrder() {
        assertTrue(killer.solveInReverseOrder());
        assertEquals(solution, killer);
    }

    @Test
    @DisplayName("should be solved in random order")
    void shouldBeSolvedInRandomOrder() {
        assertTrue(killer.solveInRandomOrder(new Random()));
        assertEquals(solution, killer);
    }

    @ParameterizedTest
    @MethodSource("allCellRowsAndColumnsForGrid")
    @DisplayName("should only have values between 1 and Killer.GRID_SIZE after successful solve")
    void shouldOnlyHaveValuesBetween1AndKillerGridSizeAfterSuccessfulSolve(final int row, final int column) {
        assumeTrue(killer.solveInNormalOrder());

        final int cell = killer.getCell(row, column);

        assertTrue(cell > 0);
        assertTrue(cell <= killer.getGridSize());
    }
}
