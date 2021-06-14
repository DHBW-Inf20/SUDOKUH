import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

public class Sudoku {

    public static final int EMPTY_CELL = 0;
    public static final int GRID_SIZE = 9;
    public static final int SUB_GRID_SIZE = 3;


    private static record Cell(int row, int column) {}


    private final int[][] grid;


    public Sudoku() {
        grid = new int[GRID_SIZE][GRID_SIZE];
        for (final int[] row : grid) {
            fill(row, EMPTY_CELL);
        }
    }

    public Sudoku(final int[][] grid) throws IllegalArgumentException {
        this.grid = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = copyOf(grid[i], grid[i].length);
        }
        if (!isValid()) throw new IllegalArgumentException("input sudoku is not valid");
    }


    public int[][] getGrid() {
        return grid;
    }


    public boolean solve() {
        return solveInternal(getNextEmptyCell(0, 0));
    }

    private boolean solveInternal(final Cell currentCell) {

        if (currentCell == null) return true; // all cells are filled (only valid fills happen) -> found solution

        // on overflow: back to 0
        final int nextCellColumn = (currentCell.column + 1 < GRID_SIZE) ? currentCell.column + 1 : 0;
        final int nextCellRow = (nextCellColumn == 0) ? currentCell.row + 1 : currentCell.row;
        final Cell nextEmptyCell = getNextEmptyCell(nextCellRow, nextCellColumn); // start with next cell (current is empty)

        for (int number = 1; number <= GRID_SIZE; number++) {
            grid[currentCell.row][currentCell.column] = number; // choose next number

            // number was valid and recursive solve was successful -> found solution
            if (constraintsFulfilled(currentCell) && solveInternal(nextEmptyCell)) return true;
        }

        grid[currentCell.row][currentCell.column] = EMPTY_CELL; // no number was valid -> undo and go back in recursion
        return false;
    }

    private Cell getNextEmptyCell(final int startRow, final int startColumn) {
        // start row with startRow
        for (int row = startRow; row < GRID_SIZE; row++) {

            // start column with startColumn (but only in first iteration of outer loop)
            for (int column = (row == startRow) ? startColumn : 0; column < GRID_SIZE; column++) {

                if (grid[row][column] == EMPTY_CELL) return new Cell(row, column);
            }
        }
        return null; // startRow >= GRID_SIZE or reached end
    }

    private boolean constraintsFulfilled(final Cell cell) {

        // check for appearance of grid[row][column] in same row/column -> if one was found immediately return false
        for (int index = 0; index < GRID_SIZE; index++) {
            if ((cell.row != index && grid[cell.row][cell.column] == grid[index][cell.column]) ||     // grid[row][column] twice in row
                    (cell.column != index && grid[cell.row][cell.column] == grid[cell.row][index])) { // grid[row][column] twice in column
                return false;
            }
        }

        // row % SUB_GRID_SIZE maps row to value between 0 and SUB_GRID_SIZE - 1 (sudoku is split into sub-grids)
        // the upper bound of one sub-grid is SUB_GRID_SIZE - 1 higher than lower bound
        // same for column
        final int rowLowerBound = cell.row - (cell.row % SUB_GRID_SIZE);
        final int rowUpperBound = rowLowerBound + SUB_GRID_SIZE - 1;
        final int columnLowerBound = cell.column - (cell.column % SUB_GRID_SIZE);
        final int columnUpperBound = columnLowerBound + SUB_GRID_SIZE - 1;

        for (int rowIndex = rowLowerBound; rowIndex <= rowUpperBound; rowIndex++) {
            for (int columnIndex = columnLowerBound; columnIndex <= columnUpperBound; columnIndex++) {

                if (!(cell.row == rowIndex && cell.column == columnIndex) &&           // don't check grid[row][column]
                        grid[cell.row][cell.column] == grid[rowIndex][columnIndex]) { // grid[row][column] twice in sub-grid
                    return false;
                }
            }
        }

        return true; // no rule was violated -> constraints fulfilled
    }

    private boolean isValid() {
        if (grid.length != GRID_SIZE) return false; // wrong amount of rows

        for (int row = 0; row < GRID_SIZE; row++) {
            if (grid[row].length != GRID_SIZE) return false; // wrong amount of columns

            for (int column = 0; column < GRID_SIZE; column++) {
                final int currentCell = grid[row][column];

                if (currentCell == EMPTY_CELL) continue; // empty cell is always ok
                if (currentCell < 1 || currentCell > GRID_SIZE || !constraintsFulfilled(new Cell(row, column))) {
                    return false;
                }
            }
        }
        return true; // no rule was violated -> valid
    }
}
