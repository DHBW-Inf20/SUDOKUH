package presenter;

import model.BasePuzzle.Cell;
import model.BasePuzzle.SetResult;
import model.Sudoku;
import view.CustomButton;
import view.LabelPanel;
import view.game_menus.GameMenu;
import view.game_menus.SolveMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class SolvePresenter {

    private final Sudoku sudoku;
    private final GameMenu gameMenu;


    public SolvePresenter(int size) {
        sudoku = new Sudoku(size);

        gameMenu = new SolveMenu(size, this::handleButtonEvent, "Sudoku lösen");
        gameMenu.setVisible(true);
    }


    // ActionListener for numbers-buttons to provide a correct input
    private void handleButtonEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        LabelPanel clickedCell = gameMenu.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    final SetResult result = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                    if (result == SetResult.INVALID_VALUE) {
                        throw new IllegalStateException("Tried to set a cell to a number that was out of the valid range: " + number);
                    } else if (result.isSuccess()) {
                        gameMenu.validInput(number);
                    } else {
                        // TODO show conflicts
                        final Set<Cell> conflicts = result.conflictingCells();
                        gameMenu.invalidInput(number);
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                    clickedCell.getLabel().setText("");
                }
            }
            case SOLVE -> {
                if (sudoku.solve()) {
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
        }
    }
}
