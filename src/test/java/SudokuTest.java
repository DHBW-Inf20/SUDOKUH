import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Arrays.deepEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuTest {

    private Sudoku cut;

    private final int[][] grid = {
            {3, 0, 0,/**/ 0, 2, 8,/**/ 0, 0, 0},
            {0, 0, 1,/**/ 7, 0, 0,/**/ 0, 9, 4},
            {7, 8, 0,/**/ 0, 0, 6,/**/ 5, 0, 0},
//          /********/**/*********/**/********/
            {0, 0, 0,/**/ 3, 5, 0,/**/ 0, 0, 8},
            {0, 9, 8,/**/ 0, 0, 0,/**/ 7, 3, 0},
            {4, 0, 0,/**/ 0, 1, 9,/**/ 0, 0, 0},
//          /********/**/*********/**/********/
            {0, 0, 4,/**/ 6, 0, 0,/**/ 0, 7, 5},
            {8, 3, 0,/**/ 0, 0, 4,/**/ 1, 0, 0},
            {0, 0, 0,/**/ 2, 7, 0,/**/ 0, 0, 9}
    };

    @BeforeEach
    public void createSudoku() {
        cut = new Sudoku(grid);
    }

    @Test
    public void testCreate() {
        assertTrue(deepEquals(grid, cut.getGrid()));
    }

    @Test
    public void testSolve() {
        assertTrue(cut.solve());
        assertTrue(
                Arrays.stream(cut.getGrid())
                        .flatMapToInt(Arrays::stream)
                        .noneMatch(it -> it <= 0 || it > Sudoku.GRID_SIZE)
        );
    }

    @Test
    public void testEmptySolve() {
        cut = new Sudoku();
        assertTrue(deepEquals(new int[Sudoku.GRID_SIZE][Sudoku.GRID_SIZE], cut.getGrid()));
    }
}
