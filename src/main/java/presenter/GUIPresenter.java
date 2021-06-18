package presenter;

import model.Sudoku;
import view.CellLabel;
import view.CustomButton;
import view.GUI;

import java.awt.event.ActionEvent;

public class GUIPresenter {

    private final Sudoku sudoku;
    private final GUI gui;


    public GUIPresenter(int size) {
        sudoku = new Sudoku(size);

        gui = new GUI(size, this::handleButtonEvent);
        gui.setVisible(true);
    }


    // ActionListener for numbers-buttons to provide a correct input
    private void handleButtonEvent(ActionEvent e) {
        CustomButton button = (CustomButton) e.getSource();
        CellLabel clickedCell = gui.getClicked();

        switch (button.getType()) {
            case NUMBER -> {
                if (!clickedCell.isPredefined()) {
                    int number = button.getValue();
                    boolean valid = sudoku.setCell(clickedCell.getRow(), clickedCell.getCol(), number);
                    if (valid) {
                        clickedCell.setText(String.valueOf(number));
                    } else {
                        // todo warning (invalid input for this cell)
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
                    int[][] grid = sudoku.getGrid();
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid[i].length; j++) {
                            gui.setValue(i, j, grid[i][j]);
                        }
                    }
                } else {
                    // todo warning (can not solve)
                }
            }
        }
    }
}
