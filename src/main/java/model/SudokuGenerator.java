package model;

import model.AbstractPuzzle.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;
import static model.AbstractPuzzle.SolveResult;

/**
 * Uninstantiable class with a static method to generate a random unsolved {@link Sudoku} and its solution.
 *
 * @author Luca Kellermann
 */
public final class SudokuGenerator {

    public static SudokuAndSolution generateSudokuAndSolution(final int subGridSize) {
        final Random random = new Random();
        final Sudoku randomSudoku = new Sudoku(subGridSize, random);
        final Sudoku solution = randomSudoku.getCopy();

        // put all cells in a list and shuffle
        final List<Cell> cells = new ArrayList<>(randomSudoku.getNumberOfCells());
        for (int row = 0; row < randomSudoku.getGridSize(); row++) {
            for (int column = 0; column < randomSudoku.getGridSize(); column++) {
                cells.add(new Cell(row, column));
            }
        }
        shuffle(cells, random);

        // reset each cell (but only if there still is a unique solution after reset)
        for (final Cell cell : cells) {
            final int previousCellValue = randomSudoku.getCell(cell.row(), cell.column());
            randomSudoku.resetCell(cell.row(), cell.column());
            if (randomSudoku.getCopy().solve() != SolveResult.ONE_SOLUTION) {
                randomSudoku.setCell(cell.row(), cell.column(), previousCellValue);
            }
        }

        return new SudokuAndSolution(randomSudoku, solution);
    }

    private SudokuGenerator() {}
}
