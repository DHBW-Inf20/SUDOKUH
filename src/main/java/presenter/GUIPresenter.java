package presenter;

import model.Sudoku;
import view.CellLabel;
import view.CustomButton;
import view.game_menus.gameMenu;

import java.awt.*;
import java.awt.event.ActionEvent;

public class GUIPresenter {

    private final Sudoku sudoku;
    private final gameMenu gameMenu;


    public GUIPresenter(int size) {
        sudoku = new Sudoku(size);

        gameMenu = new view.game_menus.solveMenu(size, this::handleButtonEvent, "Sudoku");
        gameMenu.setVisible(true);
    }


    // ActionListener for numbers-buttons to provide a correct input
    private void handleButtonEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        CellLabel clickedCell = gameMenu.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    boolean valid = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                    if (valid) {
                        gameMenu.validInput(String.valueOf(number));
                    } else {
                        gameMenu.invalidInput(String.valueOf(number));
                    }
                }
            }
            case DELETE -> {
                if (!clickedCell.isPredefined()) {
                    sudoku.resetCell(clickedCell.getRow(), clickedCell.getCol());
                    clickedCell.setText("");
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
