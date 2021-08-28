package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetCellResult;
import model.Killer;
import model.Killer.GroupsUpdateResult;
import util.GameMode;
import view.Theme;
import view.ingame.CustomButton;
import view.ingame.GroupPopUpWindow;
import view.ingame.InGameViewScaffold;
import view.ingame.LabelPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static util.Strings.*;

/**
 * @author Philipp Kremling
 */
public final class SolveKillerPresenter extends SolvePresenter {

    private boolean chooseGroup;
    private boolean editGroup;

    public SolveKillerPresenter(int size, Theme theme, boolean autoStepForward, boolean highlighting) {
        super(size, GameMode.KILLER_SOLVE, theme, highlighting, autoStepForward);
        chooseGroup = false;
        editGroup = false;
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
                        final Set<Cell> conflicts = result.conflictingCells();
                        for (Cell c : conflicts) {
                            inGameViewScaffold.highlightConflicts(c);
                        }
                        inGameViewScaffold.invalidInput(String.valueOf(number));
                        inGameViewScaffold.setGUIText(LOGICAL_WRONG_INPUT, Color.red);
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
                    case NO_SOLUTION -> inGameViewScaffold.setGUIText(THIS_PUZZLE_CANNOT_BE_SOLVED, Color.red);
                    case NOT_IN_VALID_STATE_FOR_SOLVE -> inGameViewScaffold.setGUIText(THIS_PUZZLE_CANNOT_BE_SOLVED_YET, Color.red);
                    case ONE_SOLUTION -> {
                        inGameViewScaffold.setGUIText(THE_PUZZLE_WAS_SOLVED_SUCCESSFULLY, Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                    case MULTIPLE_SOLUTIONS -> {
                        inGameViewScaffold.setGUIText(CENTER(THE_PUZZLE_WAS_SOLVED_SUCCESSFULLY + BR + BUT_THERE_WAS_MORE_THAN_ONE_POSSIBILITY), Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                }
            }
            case CHOOSE_GROUP -> {
                if (!editGroup) {
                    chooseGroup = !chooseGroup;
                    if (chooseGroup) {
                        inGameViewScaffold.setChooseMode();
                    } else {
                        ArrayList<LabelPanel> group = inGameViewScaffold.setNoChooseMode();
                        saveGroup(group);
                    }
                } else {
                    inGameViewScaffold.setGUIText(THE_SELECT_MODE_CANNOT_BE_ACTIVATED_WHILE_THE_EDIT_MODE_IS_ACTIVATED);
                }
            }
            case REMOVE_GROUP -> {
                if (chooseGroup) {
                    chooseGroup = false;
                    inGameViewScaffold.setNoChooseMode();
                } else {
                    ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                    if (group != null) {
                        if (!group.isEmpty())
                            ((Killer) sudoku).removeGroup(((Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                        for (LabelPanel l : group) {
                            sudoku.resetCell(l.getRow(), l.getCol());
                        }
                    }
                }
            }
            case EDIT_GROUP -> {
                if (!chooseGroup) {
                    editGroup = !editGroup;
                    if (editGroup) {
                        ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                        if (group != null) {
                            if (!group.isEmpty())
                                ((Killer) sudoku).removeGroup(((Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                            inGameViewScaffold.setEditMode(group);
                        } else {
                            editGroup = false;
                        }
                    } else {
                        ArrayList<LabelPanel> group = inGameViewScaffold.setNoEditMode();
                        saveGroup(group);
                    }
                } else {
                    inGameViewScaffold.setGUIText(THE_EDIT_MODE_CANNOT_BE_ACTIVATED_WHILE_THE_SELECT_MODE_IS_ACTIVATED);
                }
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

    private void saveGroup(ArrayList<LabelPanel> group) {
        if (!group.isEmpty()) {
            GroupPopUpWindow userInput = new GroupPopUpWindow(group);
            int sum = userInput.getSum();
            // False input
            if (sum == -1) {
                inGameViewScaffold.setGUIText(FAULTY_SUM, Color.red);
                // Logical incorrect input
            } else if (sum == -2) {
                inGameViewScaffold.setGUIText(LOGICAL_INCORRECT_SUM, Color.red);
            } else {
                final Set<Cell> cells = group.stream().map(it -> new Cell(it.getRow(), it.getCol())).collect(toSet());
                final GroupsUpdateResult result = ((Killer) sudoku).putCellsIntoNewGroup(cells, sum);

                if (result.isSuccess()) {
                    inGameViewScaffold.addGroup(group, sum);
                } else {
                    inGameViewScaffold.setGUIText(switch (result.failureReason()) {
                        case GROUP_VALUES_NOT_UNIQUE -> GROUPS_WITH_DUPLICATE_VALUES_ARE_NOT_ALLOWED;
                        case GROUP_SUM_NOT_VALID -> THE_SUM_IS_INVALID;
                        case GROUP_IS_EMPTY -> EMPTY_GROUPS_ARE_NOT_ALLOWED;
                        case GROUP_HAS_TOO_MANY_CELLS -> GROUPS_CAN_AT_MOST_HAVE_N_CELLS(Killer.Group.MAX_CELLS);
                        case GROUP_CELLS_ARE_NOT_CONNECTED -> THIS_WOULD_LEAD_TO_GROUPS_WITH_UNCONNECTED_CELLS;
                        case GROUP_NOT_PART_OF_KILLER -> throw new IllegalStateException("Unexpected result for Killer#putCellsIntNewGroup()");
                    }, Color.red);
                }
            }
        }
    }
}
