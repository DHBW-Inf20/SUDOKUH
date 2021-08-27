package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetCellResult;
import model.Killer;
import model.Killer.GroupsUpdateResult;
import view.CustomButton;
import view.GroupPopUpWindow;
import view.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SolveKillerPresenter extends SolvePresenter {

    private boolean chooseGroup;
    private boolean editGroup;

    public SolveKillerPresenter(int size, String theme, boolean autoStepForward, boolean highlighting) {
        super(size, util.Mode.KILLER_SOLVE, theme, highlighting, autoStepForward);
        chooseGroup = false;
        editGroup = false;
    }

    /**
     * Handles the button events and triggers actions based on the clicked button
     */
    @Override
    public void handleButton(CustomButton button) {
        LabelPanel clickedCell = inGameViewScaffold.getClicked();
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
                    case NO_SOLUTION -> inGameViewScaffold.setGUIText("Dieses Sudoku kann nicht gelöst werden!", Color.red);
                    case NOT_IN_VALID_STATE_FOR_SOLVE -> inGameViewScaffold.setGUIText("Dieses Sudoku kann noch nicht gelöst werden!", Color.red);
                    case ONE_SOLUTION -> {
                        inGameViewScaffold.setGUIText("Das Sudoku wurde erfolgreich gelöst!", Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                    case MULTIPLE_SOLUTIONS -> {
                        inGameViewScaffold.setGUIText("<html><body><center>Das Sudoku wurde erfolgreich gelöst!<br>Es gibt allerdings mehr als eine Möglichkeit.</center></body></html>", Color.green);
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                inGameViewScaffold.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                }
            }
            case CHOOSEGROUP -> {
                if (!editGroup) {
                    chooseGroup = !chooseGroup;
                    if (chooseGroup) {
                        inGameViewScaffold.setChooseMode();
                    } else {
                        ArrayList<LabelPanel> group = inGameViewScaffold.setNoChooseMode();
                        saveGroup(group);
                    }
                } else {
                    inGameViewScaffold.setGUIText("<html><body><center>Während der Bearbeitungsmodus aktiviert ist, kann der<br>Auswahlmodus nicht aktiviert werden.</center></body></html>");
                }
            }
            case REMOVEGROUP -> {
                if (chooseGroup) {
                    chooseGroup = false;
                    inGameViewScaffold.setNoChooseMode();
                } else {
                    ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                    if (group != null) {
                        if (!group.isEmpty())
                            ((model.Killer) sudoku).removeGroup(((model.Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                        for (LabelPanel l : group) {
                            sudoku.resetCell(l.getRow(), l.getCol());
                        }
                    }
                }
            }
            case EDITGROUP -> {
                if (!chooseGroup) {
                    editGroup = !editGroup;
                    if (editGroup) {
                        ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                        if (group != null) {
                            if (!group.isEmpty())
                                ((model.Killer) sudoku).removeGroup(((model.Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                            inGameViewScaffold.setEditMode(group);
                        } else {
                            editGroup = false;
                        }
                    } else {
                        ArrayList<LabelPanel> group = inGameViewScaffold.setNoEditMode();
                        saveGroup(group);
                    }
                } else {
                    inGameViewScaffold.setGUIText("<html><body><center>Während der Auswahlmodus aktiviert ist, kann der<br>Bearbeitungsmodus nicht aktiviert werden.</center></body></html>");
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
                inGameViewScaffold.setGUIText("Fehlerhafte Summe!", Color.red);
                // Logical incorrect input
            } else if (sum == -2) {
                inGameViewScaffold.setGUIText("Logisch inkorrekte Summe!", Color.red);
            } else {
                final Set<Cell> cells = group.stream().map(it -> new Cell(it.getRow(), it.getCol())).collect(toSet());
                final GroupsUpdateResult result = ((Killer) sudoku).putCellsIntoNewGroup(cells, sum);

                if (result.isSuccess()) {
                    inGameViewScaffold.addGroup(group, sum);
                } else {
                    inGameViewScaffold.setGUIText(switch (result.failureReason()) {
                        case GROUP_VALUES_NOT_UNIQUE -> "Dies würde zu Gruppen mit doppelt vorkommenden Werten führen!";
                        case GROUP_SUM_NOT_VALID -> "Die Summe ist ungültig!";
                        case GROUP_IS_EMPTY -> "Es können keine leeren Gruppen hinzugefügt werden!";
                        case GROUP_HAS_TOO_MANY_CELLS -> "Gruppen können maximal " + Killer.Group.MAX_CELLS + " Feldern haben!";
                        case GROUP_CELLS_ARE_NOT_CONNECTED -> "Dies würde zu Gruppen mit nicht zusammenhängenden Feldern führen!";
                        case GROUP_NOT_PART_OF_KILLER -> throw new IllegalStateException("Unexpected result for Killer#putCellsIntNewGroup()");
                    }, Color.red);
                }
            }
        }
    }
}
