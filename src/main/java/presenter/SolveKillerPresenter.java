package presenter;

import model.AbstractPuzzle;
import model.Killer;
import view.CustomButton;
import view.GroupPopUpWindow;
import view.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class SolveKillerPresenter extends SolvePresenter {

    private boolean chooseGroup;

    public SolveKillerPresenter(int size, String theme, boolean autoStepForward, boolean highlighting) {
        super(size, util.Mode.KILLER_SOLVE, theme, highlighting, autoStepForward);
        chooseGroup = false;
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
            case CHOOSEGROUP -> {
                chooseGroup = !chooseGroup;
                if(chooseGroup) {
                    inGameViewScaffold.setChooseMode();
                } else {
                    GroupPopUpWindow userInput = new GroupPopUpWindow(inGameViewScaffold);
                    int sum = userInput.getSum();
                    ArrayList<LabelPanel> group = inGameViewScaffold.setNoChooseMode();
                    model.Killer.Group cellGroup = null;
                    for(LabelPanel l : group) {
                        if(cellGroup != null) {
                            ((Killer)sudoku).putCellIntoExistingGroup(l.getRow(), l.getCol(), cellGroup);
                        } else {
                            ((model.Killer)sudoku).putCellIntoNewGroup(l.getRow(), l.getCol(), sum);
                        }
                        cellGroup = ((model.Killer)sudoku).getGroupForCell(l.getRow(), l.getCol());
                    }
                    inGameViewScaffold.addGroup(group,sum);
                }
            }
            case REMOVEGROUP -> {
                if(chooseGroup) {
                    chooseGroup = false;
                    inGameViewScaffold.setNoChooseMode();
                } else {
                    ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                    for(LabelPanel l : group) {
                        ((model.Killer)sudoku).removeCellFromGroup(l.getRow(), l.getCol());
                        sudoku.resetCell(l.getRow(), l.getCol());
                    }
                }
            }
        }
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }
}
