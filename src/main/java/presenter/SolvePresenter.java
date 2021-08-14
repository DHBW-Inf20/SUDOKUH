package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetResult;
import model.Killer;
import model.Str8ts;
import model.Sudoku;
import util.KeyInputListener;
import view.CustomButton;
import view.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public abstract class SolvePresenter implements Presenter{

    protected final AbstractPuzzle sudoku;
    protected view.ingame.InGameViewScaffold inGameViewScaffold;

    protected boolean autoStepForward;

    public SolvePresenter(int size, util.Mode gameMode, String theme, boolean autoStepForward) {
        this.autoStepForward = autoStepForward;
        switch (gameMode) {
            case STRAITS_SOLVE -> {
                sudoku = new Str8ts();
                inGameViewScaffold = new InGameViewScaffold(size, this::handleButtonListenerEvent, "Str8ts lösen", theme, gameMode);
            }
            case KILLER_SOLVE -> {
                sudoku = new Killer();
                inGameViewScaffold = new InGameViewScaffold(size, this::handleButtonListenerEvent, "Killer lösen", theme, gameMode);
            }
            default -> {
                sudoku = new Sudoku(size);
                inGameViewScaffold = new InGameViewScaffold(size, this::handleButtonListenerEvent, "Sudoku lösen", theme, gameMode);
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
    public void handleButton(CustomButton button){
        LabelPanel clickedCell = inGameViewScaffold.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                    if (result == SetResult.INVALID_VALUE) {
                        throw new IllegalStateException("Tried to set a cell to a number that was out of the valid range: " + number);
                    } else if (result.isSuccess()) {
                        inGameViewScaffold.validInput(String.valueOf(number));
                    } else {
                        final Set<Cell> conflicts = result.conflictingCells();
                        for(Cell c : conflicts) {
                            inGameViewScaffold.highlightConflicts(c);
                        }
                        inGameViewScaffold.invalidInput(String.valueOf(number));
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
                switch (sudoku.solve()) {
                    case NO_SOLUTION -> inGameViewScaffold.setGUIText("Dieses Sudoku kann nicht gelöst werden!", Color.red);
                    // todo handle cases differently (for example warning when solution is not unique)
                    case MULTIPLE_SOLUTIONS, ONE_SOLUTION -> {
                        inGameViewScaffold.resetGUIText();
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
