package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetResult;
import model.Sudoku;
import model.SudokuAndSolution;
import model.SudokuGenerator;
import util.KeyInputListener;
import view.CustomButton;
import view.LabelPanel;
import view.game_menus.GameMenu;
import view.game_menus.PlayMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class PlayPresenter implements Presenter{

    protected final AbstractPuzzle sudoku;
    protected final AbstractPuzzle solution;
    protected final view.game_menus.PlayMenu gameMenu;

    protected int tipLimit;
    protected int tipsUsed;

    public PlayPresenter(int size, String theme, int tipLimit) {
        SudokuAndSolution sudokuAndSolution = SudokuGenerator.generateSudokuAndSolution(size);
        sudoku = sudokuAndSolution.sudoku();
        solution = sudokuAndSolution.solution();

        gameMenu = new view.game_menus.PlayMenu(size, this::handleButtonListenerEvent, "Sudoku", theme);
        gameMenu.setVisible(true);

        gameMenu.addKeyListener(new KeyInputListener(this));

        this.setPredefinedCells();

        this.tipLimit = tipLimit;
        this.tipsUsed = 0;
        gameMenu.setRemainingTips(tipLimit-tipsUsed);
    }

    /**
     * Getting all predefined cells and setting it to the gui
     */
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

    /**
     * If sudoku is solved right, print a text to the gui
     */
    private boolean verifySolution() {
        if (sudoku.equals(solution)) {
            gameMenu.setGUIText("Korrekte Lösung!", Color.green);
            return true;
        }
        return false;
    }

    /**
     * Action listener for buttons
     */
    public void handleButtonListenerEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        handleButton(button);
    }

    /**
     * Handles the button events and triggers actions based on the clicked button
     */
    @Override
    public void handleButton(CustomButton button) {
        LabelPanel clickedCell = gameMenu.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                int number = button.getValue();
                if (!clickedCell.isPredefined()) {
                    if (gameMenu.getNoteMode()) {
                        gameMenu.setNote(number);
                    } else {
                        final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                        if (result != SetResult.INVALID_VALUE) {
                            gameMenu.validInput(number);
                            this.verifySolution();
                        } else {
                            final Set<Cell> conflicts = result.conflictingCells();
                            for(Cell c : conflicts) {
                                gameMenu.highlightConflicts(c);
                            }
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
                tipsUsed++;
                if(tipLimit >= tipsUsed) {
                    gameMenu.validInput(String.valueOf(solution.getCell(clickedCell.getRow(), clickedCell.getCol())), (tipLimit-tipsUsed));
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
                    if(tipsUsed == tipLimit) gameMenu.reachedMaxTips();
                } else {
                    gameMenu.reachedMaxTips();
                }
            }
            case VERIFY -> {
                if (!this.verifySolution()) {
                    gameMenu.setGUIText("Diese Lösung ist falsch oder unvollständig!", Color.red);
                }
            }
            case PEN -> gameMenu.changeNoteMode(button);
        }
    }
    public GameMenu getGameMenu() {
        return gameMenu;
    }
}
