package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.SetCellResult;
import model.Str8ts;
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
public final class SolveStr8tsPresenter extends SolvePresenter {
    public SolveStr8tsPresenter(int gridSize, Theme theme, boolean autoStepForward, boolean highlighting) {
        super(gridSize, GameMode.STR8TS_SOLVE, theme, highlighting, autoStepForward);
    }

    /**
     * Action listener for buttons
     */
    @Override
    public void handleButtonListenerEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        handleButton(button);
    }

    /**
     * Handles the button events and triggers actions based on the clicked button
     */
    @Override
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
                        final Set<AbstractPuzzle.Cell> conflicts = result.conflictingCells();
                        for (AbstractPuzzle.Cell c : conflicts) {
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
                    case NO_SOLUTION -> inGameViewScaffold.setInfoText(THIS_PUZZLE_CANNOT_BE_SOLVED, Color.red);
                    case NOT_IN_VALID_STATE_FOR_SOLVE -> inGameViewScaffold.setInfoText(THIS_PUZZLE_CANNOT_BE_SOLVED_YET, Color.red);
                    case ONE_SOLUTION -> {
                        inGameViewScaffold.setInfoText(THE_PUZZLE_WAS_SOLVED_SUCCESSFULLY, Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                    case MULTIPLE_SOLUTIONS -> {
                        inGameViewScaffold.setInfoText(CENTER(THE_PUZZLE_WAS_SOLVED_SUCCESSFULLY + BR + BUT_THERE_WAS_MORE_THAN_ONE_POSSIBILITY), Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                }
            }
            case CHANGE_COLOR -> {
                Str8ts.Color color = inGameViewScaffold.changeAndGetColor();
                ((Str8ts) sudoku).setColor(clickedCell.getRow(), clickedCell.getColumn(), color);
            }
        }
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }

    @Override
    public boolean isNoteModeActivated() {
        return false;
    }
}
