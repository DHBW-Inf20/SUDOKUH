package model;

/**
 * A {@link Sudoku} and its solution in the form of an ordered nominal Pair {@code (sudoku, solution)}.
 *
 * @author Luca Kellermann
 */
public final record SudokuAndSolution(Sudoku sudoku, Sudoku solution) {}
