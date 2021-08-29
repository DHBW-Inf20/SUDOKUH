package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetCellResult;
import model.Killer;
import model.Str8ts;
import model.Sudoku;
import util.GameMode;
import view.Theme;
import view.ingame.CustomButton;
import view.ingame.InGameViewScaffold;
import view.ingame.CellPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

import static util.Strings.*;

/**
 * @author Philipp Kremling
 */
public abstract class SolvePresenter implements Presenter {

    protected final AbstractPuzzle sudoku;
    protected InGameViewScaffold inGameViewScaffold;

    protected boolean autoStepForward;

    public SolvePresenter(int subGridSize, GameMode gameMode, Theme theme, boolean highlighting, boolean autoStepForward) {
        this.autoStepForward = autoStepForward;
        switch (gameMode) {
            case STR8TS_SOLVE -> {
                sudoku = new Str8ts();
                inGameViewScaffold = new InGameViewScaffold(subGridSize, this::handleButtonListenerEvent, SOLVE_STR8TS, theme, highlighting, autoStepForward, gameMode);
            }
            case KILLER_SOLVE -> {
                sudoku = new Killer();
                inGameViewScaffold = new InGameViewScaffold(subGridSize, this::handleButtonListenerEvent, SOLVE_KILLER, theme, highlighting, autoStepForward, gameMode);
            }
            default -> {
                sudoku = new Sudoku(subGridSize);
                inGameViewScaffold = new InGameViewScaffold(subGridSize, this::handleButtonListenerEvent, SOLVE_SUDOKU, theme, highlighting, autoStepForward, gameMode);
            }
        }
        inGameViewScaffold.addKeyListener(new KeyInputListener(this, autoStepForward));
    }

    /**
     * Action listener for buttons
     */
    public void handleButtonListenerEvent(ActionEvent e) {
        handleButton((CustomButton) e.getSource());
    }

    /**
     * Handles the button events and triggers actions based on the clicked button
     */
    public void handleButton(CustomButton button) {
        CellPanel clickedCell = inGameViewScaffold.getClicked();

        inGameViewScaffold.resetInfoText();
        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    final SetCellResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getColumn(), number);
                    if (result == SetCellResult.INVALID_VALUE) {
                        throw new IllegalStateException("Tried to set a cell to a number that was out of the valid range: " + number);
                    } else if (result.isSuccess()) {
                        inGameViewScaffold.validInput(String.valueOf(number));
                    } else {
                        final Set<Cell> conflicts = result.conflictingCells();
                        for (Cell c : conflicts) {
                            inGameViewScaffold.highlightConflicts(c);
                        }
                        inGameViewScaffold.invalidInput(String.valueOf(number));
                        inGameViewScaffold.setInfoText(LOGICAL_WRONG_INPUT, Color.red);
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getColumn());
                    inGameViewScaffold.resetCell();
                }
            }
            case SOLVE -> {
                AbstractPuzzle.SolveResult solveResult = sudoku.solve();
                switch (solveResult) {
                    case NO_SOLUTION -> inGameViewScaffold.setInfoText(THIS_SUDOKU_CANNOT_BE_SOLVED, Color.red);
                    case NOT_IN_VALID_STATE_FOR_SOLVE -> inGameViewScaffold.setInfoText(THIS_SUDOKU_CANNOT_BE_SOLVED_YET, Color.red);
                    case ONE_SOLUTION -> {
                        inGameViewScaffold.setInfoText(THE_SUDOKU_WAS_SOLVED_SUCCESSFULLY, Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                    case MULTIPLE_SOLUTIONS -> {
                        inGameViewScaffold.setInfoText(CENTER(THE_SUDOKU_WAS_SOLVED_SUCCESSFULLY + BR + BUT_THERE_WAS_MORE_THAN_ONE_POSSIBILITY), Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                }
            }
        }
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }
}
