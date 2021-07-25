package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetResult;
import model.Str8ts;
import model.Sudoku;
import view.CustomButton;
import view.LabelPanel;
import view.game_menus.GameMenu;
import view.game_menus.SolveMenu;
import view.game_menus.SolveStr8tsMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class SolvePresenter {

    protected final AbstractPuzzle sudoku;
    protected final GameMenu gameMenu;


    public SolvePresenter(int size) {
        sudoku = new Sudoku(size);

        gameMenu = new SolveMenu(size, this::handleButtonEvent, "Sudoku lösen");
        gameMenu.setVisible(true);
    }

    public SolvePresenter(int size, String gamemode) {
        if(gamemode.equals("Str8ts")) {
            sudoku = new Str8ts();
            gameMenu = new SolveStr8tsMenu(size, this::handleButtonEvent, "Str8ts lösen");
            gameMenu.setVisible(true);
        } else {
            sudoku = new Str8ts();
            gameMenu = new SolveMenu(size, this::handleButtonEvent, "Killer lösen");
            gameMenu.setVisible(true);
        }
    }

    // ActionListener for numbers-buttons to provide a correct input
    protected void handleButtonEvent(ActionEvent e) {
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
                        final Set<Cell> conflicts = result.conflictingCells();
                        for(Cell c : conflicts) {
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
                switch (sudoku.solve()) {
                    case NO_SOLUTION -> gameMenu.setGUIText("Dieses Sudoku kann nicht gelöst werden!", Color.red);
                    // todo handle cases differently (for example warning when solution is not unique)
                    case MULTIPLE_SOLUTIONS, ONE_SOLUTION -> {
                        gameMenu.resetGUIText();
                        for (int row = 0; row < sudoku.getGridSize(); row++) {
                            for (int column = 0; column < sudoku.getGridSize(); column++) {
                                gameMenu.setValue(row, column, sudoku.getCell(row, column));
                            }
                        }
                    }
                }
            }
        }
    }
}
