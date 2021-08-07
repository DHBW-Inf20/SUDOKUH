package presenter;

import model.AbstractPuzzle;
import model.AbstractPuzzle.Cell;
import model.AbstractPuzzle.SetResult;
import model.Killer;
import model.Str8ts;
import model.Sudoku;
import util.KeyInputListener;
import view.CustomButton;
import view.LabelPanel;
import view.game_menus.GameMenu;
import view.game_menus.SolveKillerMenu;
import view.game_menus.SolveMenu;
import view.game_menus.SolveStr8tsMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public abstract class SolvePresenter implements Presenter{

    protected final AbstractPuzzle sudoku;
    protected final GameMenu gameMenu;

    public SolvePresenter(int size, util.Mode gameMode, String theme) {
        switch (gameMode) {
            case STRAITS_SOLVE -> {
                sudoku = new Str8ts();
                gameMenu = new SolveStr8tsMenu(size, this::handleButtonListenerEvent, "Str8ts lösen", theme);
                            }
            case KILLER_SOLVE -> {
                sudoku = new Killer();
                gameMenu = new SolveKillerMenu(size, this::handleButtonListenerEvent, "Killer lösen", theme);
            }
            default -> {
                sudoku = new Sudoku(size);
                gameMenu = new SolveMenu(size, this::handleButtonListenerEvent, "Sudoku lösen", theme);
            }
        }
        gameMenu.setVisible(true);

        gameMenu.addKeyListener(new KeyInputListener(this));
    }

    // ActionListener for numbers-buttons to provide a correct input
    public void handleButtonListenerEvent(ActionEvent e) {
        handleButton((CustomButton) e.getSource());
    }

    // Changes after button input
    public void handleButton(CustomButton button){
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
    public GameMenu getGameMenu() {
        return gameMenu;
    }
}
