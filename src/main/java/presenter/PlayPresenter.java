package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetResult;
import model.Sudoku;
import model.SudokuAndSolution;
import model.SudokuGenerator;
import util.KeyInputListener;
import util.Mode;
import view.CustomButton;
import view.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class PlayPresenter implements Presenter{

    protected final AbstractPuzzle sudoku;
    protected final AbstractPuzzle solution;
    protected final view.ingame.InGameViewScaffold inGameViewScaffold;

    protected int tipLimit;
    protected int tipsUsed;

    protected boolean autoStepForward;

    public PlayPresenter(int size, String theme, boolean autoStepForward, int tipLimit) {
        SudokuAndSolution sudokuAndSolution = SudokuGenerator.generateSudokuAndSolution(size);
        sudoku = sudokuAndSolution.sudoku();
        solution = sudokuAndSolution.solution();

        inGameViewScaffold = new InGameViewScaffold(size, this::handleButtonListenerEvent, "Spielen", theme, Mode.SUDOKU_PLAY);

        inGameViewScaffold.addKeyListener(new KeyInputListener(this, autoStepForward));

        this.setPredefinedCells();

        this.tipLimit = tipLimit;
        this.tipsUsed = 0;
        this.autoStepForward = autoStepForward;
        inGameViewScaffold.setRemainingTips(tipLimit-tipsUsed);
    }

    /**
     * Getting all predefined cells and setting it to the gui
     */
    private void setPredefinedCells() {
        for (int i = 0; i < sudoku.getGridSize(); i++) {
            for (int j = 0; j < sudoku.getGridSize(); j++) {
                int value = sudoku.getCell(i, j);
                if (value != Sudoku.EMPTY_CELL) {
                    inGameViewScaffold.setPredefined(i, j, value);
                }
            }
        }
    }

    /**
     * If sudoku is solved right, print a text to the gui
     */
    private boolean verifySolution() {
        if (sudoku.equals(solution)) {
            inGameViewScaffold.setGUIText("Korrekte Lösung!", Color.green);
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
        LabelPanel clickedCell = inGameViewScaffold.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                int number = button.getValue();
                if (!clickedCell.isPredefined()) {
                    if (inGameViewScaffold.getNoteMode()) {
                        inGameViewScaffold.setNote(number);
                    } else {
                        final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                        if (result == SetResult.SUCCESS) {
                            inGameViewScaffold.validInput(String.valueOf(number));
                            this.verifySolution();
                        } else {
                            final Set<Cell> conflicts = result.conflictingCells();
                            for(Cell c : conflicts) {
                                inGameViewScaffold.highlightConflicts(c);
                            }
                            inGameViewScaffold.invalidInput(String.valueOf(number));
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
                if(! clickedCell.isPredefined()) {
                    tipsUsed++;
                    if (tipLimit >= tipsUsed) {
                        inGameViewScaffold.validInput(String.valueOf(solution.getCell(clickedCell.getRow(), clickedCell.getCol())), (tipLimit - tipsUsed));
                        inGameViewScaffold.setPredefined(clickedCell.getRow(), clickedCell.getCol(), solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                        final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), solution.getCell(clickedCell.getRow(), clickedCell.getCol()));
                        if (!result.isSuccess()) {
                            final Set<Cell> conflicts = result.conflictingCells();
                            if (!conflicts.isEmpty()) {
                                // can be any conflict (no order guaranteed)
                                for (Cell c : conflicts) {
                                    inGameViewScaffold.invalidInput(c.row(), c.column());
                                }
                            }
                        }
                        this.verifySolution();
                        if (tipsUsed == tipLimit) inGameViewScaffold.reachedMaxTips();
                    } else {
                        inGameViewScaffold.reachedMaxTips();
                    }
                }
            }
            case VERIFY -> {
                if (!this.verifySolution()) {
                    inGameViewScaffold.setGUIText("Diese Lösung ist falsch oder unvollständig!", Color.red);
                }
            }
            case PEN -> inGameViewScaffold.changeNoteMode(button);
        }
    }
    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }
}
