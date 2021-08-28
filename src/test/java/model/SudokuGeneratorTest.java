package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author Luca Kellermann
 */
@DisplayName("SudokuGenerator")
class SudokuGeneratorTest {

    @Test
    @DisplayName("should generate a Sudoku which has only one Solution")
    void shouldGenerateASudokuWhichHasOnlyOneSolution() {
        final SudokuGenerator.SudokuAndSolution sudokuAndSolution = SudokuGenerator.generateSudokuAndSolution(3);

        final Sudoku firstCopy = sudokuAndSolution.sudoku().getCopy();
        final Sudoku secondCopy = sudokuAndSolution.sudoku().getCopy();
        final Sudoku thirdCopy = sudokuAndSolution.sudoku().getCopy();

        assumeTrue(firstCopy.solveInNormalOrder());
        assumeTrue(secondCopy.solveInReverseOrder());
        assumeTrue(thirdCopy.solveInRandomOrder(new Random()));

        assertEquals(firstCopy, sudokuAndSolution.solution());
        assertEquals(secondCopy, sudokuAndSolution.solution());
        assertEquals(thirdCopy, sudokuAndSolution.solution());
    }
}
