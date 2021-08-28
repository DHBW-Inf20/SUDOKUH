package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.SetCellResult;
import view.ingame.CustomButton;
import view.ingame.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

/**
 * @author Philipp Kremling
 */
public class SolveStr8tsPresenter extends SolvePresenter {
    public SolveStr8tsPresenter(int size, String theme, boolean autoStepForward, boolean highlighting) {
        super(size, util.Mode.STRAITS_SOLVE, theme, highlighting, autoStepForward);
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
        LabelPanel clickedCell = inGameViewScaffold.getClicked();
        inGameViewScaffold.resetGUIText();
        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    final SetCellResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
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
                        inGameViewScaffold.setGUIText("Logisch falscher Input!", Color.red);
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                    inGameViewScaffold.resetCell();
                }
            }
            case SOLVE -> {
                AbstractPuzzle.SolveResult solveResult = sudoku.solve();
                switch (solveResult) {
                    case NO_SOLUTION -> inGameViewScaffold.setGUIText("Dieses Sudoku kann nicht gel\u00f6st werden!", Color.red);
                    case NOT_IN_VALID_STATE_FOR_SOLVE -> inGameViewScaffold.setGUIText("Dieses Sudoku kann noch nicht gel\u00f6st werden!", Color.red);
                    case ONE_SOLUTION -> {
                        inGameViewScaffold.setGUIText("Das Sudoku wurde erfolgreich gel\u00f6st!", Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                    case MULTIPLE_SOLUTIONS -> {
                        inGameViewScaffold.setGUIText("<html><body><center>Das Sudoku wurde erfolgreich gel\u00f6st!<br>Es gibt allerdings mehr als eine M\u00f6glichkeit.</center></body></html>", Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                }
            }
            case CHANGECOLOR -> {
                model.Str8ts.Color color = inGameViewScaffold.changeColor();
                ((model.Str8ts) sudoku).setColor(clickedCell.getRow(), clickedCell.getCol(), color);
            }
        }
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }

    @Override
    public boolean getNoteMode() {
        return false;
    }
}
