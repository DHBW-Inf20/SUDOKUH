package presenter;

import model.AbstractPuzzle;
import view.CustomButton;
import view.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

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
    public void handleButton(CustomButton button){
        LabelPanel clickedCell = inGameViewScaffold.getClicked();
        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    final AbstractPuzzle.SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                    if (result == AbstractPuzzle.SetResult.INVALID_VALUE) {
                        throw new IllegalStateException("Tried to set a cell to a number that was out of the valid range: " + number);
                    } else if (result.isSuccess()) {
                        inGameViewScaffold.validInput(String.valueOf(number));
                    } else {
                        final Set<AbstractPuzzle.Cell> conflicts = result.conflictingCells();
                        for(AbstractPuzzle.Cell c : conflicts) {
                            inGameViewScaffold.highlightConflicts(c);
                        }
                        inGameViewScaffold.invalidInput(String.valueOf(number));
                        inGameViewScaffold.setGUIText("Logisch falscher Input!",Color.red);
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
                if (sudoku.solve() != AbstractPuzzle.SolveResult.NO_SOLUTION) {
                    inGameViewScaffold.resetGUIText();
                    for (int row = 0; row < sudoku.getGridSize(); row++) {
                        for (int column = 0; column < sudoku.getGridSize(); column++) {
                            inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                        }
                    }
                } else {
                    inGameViewScaffold.setGUIText("Dieses Sudoku kann nicht gelöst werden!", Color.red);
                }
            }
            case CHANGECOLOR -> {
                model.Str8ts.Color color = inGameViewScaffold.changeColor();
                ((model.Str8ts)sudoku).setColor(clickedCell.getRow(), clickedCell.getCol(), color);
            }
        }
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }
}
