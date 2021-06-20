package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

public class SudokuGenerator {

    private static record Cell(int row, int column) {}

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

        // reset each cell (but only if it still has a unique solution after reset)
        for (final Cell cell : cells) {
            final int previousCellValue = randomSudoku.getCell(cell.row, cell.column);
            randomSudoku.resetCell(cell.row, cell.column);
            if (!canBeSolvedInOnlyOneWay(randomSudoku)) {
                randomSudoku.setCell(cell.row, cell.column, previousCellValue);
            }
        }

        return new SudokuAndSolution(randomSudoku, solution);
    }

    private static boolean canBeSolvedInOnlyOneWay(final Sudoku sudoku) {
        final Sudoku firstCopy = sudoku.getCopy();
        final Sudoku secondCopy = sudoku.getCopy();
        if (!(firstCopy.solve() && secondCopy.solveInReverseOrder())) {
            return false;
        }
        return firstCopy.equals(secondCopy);
    }
}
