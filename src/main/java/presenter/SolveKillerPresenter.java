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
import view.ingame.CellPanel;

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

    public SolveKillerPresenter(int gridSize, Theme theme, boolean autoStepForward, boolean highlighting) {
        super(gridSize, GameMode.KILLER_SOLVE, theme, highlighting, autoStepForward);
        chooseGroup = false;
        editGroup = false;
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
                        inGameViewScaffold.setInfoText(LOGICAL_WRONG_INPUT, Color.red);
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
            case CHOOSE_GROUP -> {
                if (!editGroup) {
                    chooseGroup = !chooseGroup;
                    if (chooseGroup) {
                        inGameViewScaffold.startChooseGroupMode();
                    } else {
                        ArrayList<CellPanel> group = inGameViewScaffold.endChooseGroupModeAndGetGroup();
                        saveGroup(group);
                    }
                } else {
                    inGameViewScaffold.setInfoText(THE_SELECT_MODE_CANNOT_BE_ACTIVATED_WHILE_THE_EDIT_MODE_IS_ACTIVATED);
                }
            }
            case REMOVE_GROUP -> {
                if (chooseGroup) {
                    chooseGroup = false;
                    inGameViewScaffold.endChooseGroupModeAndGetGroup();
                } else {
                    ArrayList<CellPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                    if (group != null) {
                        if (!group.isEmpty())
                            ((Killer) sudoku).removeGroup(((Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                        for (CellPanel cellPanel : group) {
                            sudoku.resetCell(cellPanel.getRow(), cellPanel.getCol());
                        }
                    }
                }
            }
            case EDIT_GROUP -> {
                if (!chooseGroup) {
                    editGroup = !editGroup;
                    if (editGroup) {
                        ArrayList<CellPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                        if (group != null) {
                            if (!group.isEmpty())
                                ((Killer) sudoku).removeGroup(((Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                            inGameViewScaffold.startEditGroupMode(group);
                        } else {
                            editGroup = false;
                        }
                    } else {
                        ArrayList<CellPanel> group = inGameViewScaffold.endEditGroupModeAndGetGroup();
                        saveGroup(group);
                    }
                } else {
                    inGameViewScaffold.setInfoText(THE_EDIT_MODE_CANNOT_BE_ACTIVATED_WHILE_THE_SELECT_MODE_IS_ACTIVATED);
                }
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

    private void saveGroup(ArrayList<CellPanel> group) {
        if (!group.isEmpty()) {
            int sum = new GroupPopUpWindow().getSum();
            // False input
            if (sum == -1) {
                inGameViewScaffold.setInfoText(FAULTY_SUM, Color.red);
            } else {
                final Set<Cell> cells = group.stream().map(it -> new Cell(it.getRow(), it.getCol())).collect(toSet());
                final GroupsUpdateResult result = ((Killer) sudoku).putCellsIntoNewGroup(cells, sum);

                if (result.isSuccess()) {
                    inGameViewScaffold.addGroup(group, sum);
                } else {
                    inGameViewScaffold.setInfoText(switch (result.failureReason()) {
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
