package presenter;

import model.AbstractPuzzle;
import model.Killer;
import util.Type;
import view.CustomButton;
import view.GroupPopUpWindow;
import view.LabelPanel;
import view.ingame.InGameViewScaffold;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

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
                if(!editGroup) {
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
                if(chooseGroup) {
                    chooseGroup = false;
                    inGameViewScaffold.setNoChooseMode();
                } else {
                    ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                    if(group != null) {
                        if (!group.isEmpty())
                            ((model.Killer) sudoku).removeGroup(((model.Killer) sudoku).getGroupForCell(group.get(0).getRow(), group.get(0).getCol()));
                        for (LabelPanel l : group) {
                            sudoku.resetCell(l.getRow(), l.getCol());
                        }
                    }
                }
            }
            case EDITGROUP -> {
                if(!chooseGroup) {
                    editGroup = !editGroup;
                    if (editGroup) {
                        ArrayList<LabelPanel> group = inGameViewScaffold.removeGroup(clickedCell);
                        if(group != null) {
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

    private void saveGroup(ArrayList<LabelPanel> group) {
        model.Killer.Group cellGroup = null;
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
                for (LabelPanel l : group) {
                    if (cellGroup != null) {
                        if (((Killer) sudoku).putCellIntoExistingGroup(l.getRow(), l.getCol(), cellGroup).isSuccess()) {
                            inGameViewScaffold.addGroup(group, sum);
                        } else {
                            inGameViewScaffold.setGUIText("Logisch inkorrekte Summe!", Color.red);
                        }
                    } else {
                        if (((model.Killer) sudoku).putCellIntoNewGroup(l.getRow(), l.getCol(), sum).isSuccess()) {
                            inGameViewScaffold.addGroup(group, sum);
                        } else {
                            inGameViewScaffold.setGUIText("Logisch inkorrekte Summe!", Color.red);
                        }
                    }
                    cellGroup = ((model.Killer) sudoku).getGroupForCell(l.getRow(), l.getCol());
                }
            }
        }
    }
}
