package presenter;

import model.AbstractPuzzle;
import view.CustomButton;
import view.LabelPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class SolveStr8tsPresenter extends SolvePresenter {
    public SolveStr8tsPresenter(int size) {
        super(size, "Str8ts lösen");

        this.setColoredCells();
    }

    public void setColoredCells() {

    }

    @Override
    // ActionListener for numbers-buttons to provide a correct input
    protected void handleButtonEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        LabelPanel clickedCell = gameMenu.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    final AbstractPuzzle.SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                    if (result == AbstractPuzzle.SetResult.INVALID_VALUE) {
                        throw new IllegalStateException("Tried to set a cell to a number that was out of the valid range: " + number);
                    } else if (result.isSuccess()) {
                        gameMenu.validInput(number);
                    } else {
                        final Set<AbstractPuzzle.Cell> conflicts = result.conflictingCells();
                        for(AbstractPuzzle.Cell c : conflicts) {
                            gameMenu.highlightConflicts(c);
                        }
                        gameMenu.invalidInput(number);
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                    gameMenu.resetCell();
                }
            }
            case SOLVE -> {
                if (sudoku.solve() != AbstractPuzzle.SolveResult.NO_SOLUTION) {
                    gameMenu.resetGUIText();
                    for (int row = 0; row < sudoku.getGridSize(); row++) {
                        for (int column = 0; column < sudoku.getGridSize(); column++) {
                            gameMenu.setValue(row, column, sudoku.getCell(row, column));
                        }
                    }
                } else {
                    gameMenu.setGUIText("Dieses Sudoku kann nicht gelöst werden!", Color.red);
                }
            }
            case CHANGECOLOR -> {
                gameMenu.changeColor();
            }
        }
    }
}
