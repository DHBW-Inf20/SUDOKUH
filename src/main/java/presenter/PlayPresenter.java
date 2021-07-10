package presenter;

import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetResult;
import model.Sudoku;
import model.SudokuAndSolution;
import model.SudokuGenerator;
import view.CustomButton;
import view.LabelPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class PlayPresenter {

    private final Sudoku sudoku;
    private final Sudoku solution;
    private final view.game_menus.PlayMenu gameMenu;


    public PlayPresenter(int size) {
        SudokuAndSolution sudokuAndSolution = SudokuGenerator.generateSudokuAndSolution(size);
        sudoku = sudokuAndSolution.sudoku();
        solution = sudokuAndSolution.solution();

        gameMenu = new view.game_menus.PlayMenu(size, this::handleButtonEvent, "Sudoku spielen");
        gameMenu.setVisible(true);

        this.setPredefinedCells();
    }

    private void setPredefinedCells() {
        for (int i = 0; i < sudoku.getGridSize(); i++) {
            for (int j = 0; j < sudoku.getGridSize(); j++) {
                int value = sudoku.getCell(i, j);
                if (value != Sudoku.EMPTY_CELL) {
                    gameMenu.setPredefined(i, j, value);
                }
            }
        }
    }

    private boolean verifySolution() {
        if (sudoku.equals(solution)) {
            gameMenu.setGUIText("Korrekte Lösung!", Color.green);
            return true;
        }
        return false;
    }

    // ActionListener for numbers-buttons to provide a correct input
    private void handleButtonEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        LabelPanel clickedCell = gameMenu.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                int number = button.getValue();
                if (!clickedCell.isPredefined()) {
                    if (gameMenu.getNoteMode()) {
                        gameMenu.setNote(number);
                    } else {
                        final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                        if (result == SetResult.INVALID_VALUE) {
                            throw new IllegalStateException("Tried to set a cell to a number that was out of the valid range: " + number);
                        } else if (result.isSuccess()) {
                            gameMenu.validInput(number);
                            this.verifySolution();
                        } else {
                            // TODO show conflicts
                            final Set<Cell> conflicts = result.conflictingCells();
                            gameMenu.invalidInput(number);
                        }
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                    clickedCell.setText("");
                }
            }
            case TIP -> {
                gameMenu.validInput(solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                gameMenu.setPredefined(clickedCell.getRow(), clickedCell.getCol(), solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                // TODO: Setcell gibt Setresult zurück: boolean und ggf. Zeile & Spalte -> Markieren von selbst eingegebenem Fehler
                final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                if (!result.isSuccess()) {
                    // TODO show conflicts
                    final Set<Cell> conflicts = result.conflictingCells();
                    if (!conflicts.isEmpty()) {
                        // can be any conflict (no order guaranteed) -> TODO show all
                        final Cell randomConflict = conflicts.iterator().next();
                        gameMenu.invalidInput(randomConflict.row(), randomConflict.column());
                    }
                }
                this.verifySolution();
            }
            case VERIFY -> {
                if (!this.verifySolution()) {
                    gameMenu.setGUIText("Diese Lösung ist falsch oder unvollständig!", Color.red);
                }
            }
            case PEN -> gameMenu.changeNoteMode(button);
        }
    }
}
