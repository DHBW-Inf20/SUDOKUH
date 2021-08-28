package presenter;

import util.GameMode;
import view.Theme;
import view.ingame.InGameViewScaffold;

/**
 * @author Philipp Kremling
 */
public class SolveSudokuPresenter extends SolvePresenter {
    public SolveSudokuPresenter(int size, Theme theme, boolean autoStepForward, boolean highlighting) {
        super(size, GameMode.SUDOKU_SOLVE, theme, autoStepForward, highlighting);
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }

    @Override
    public boolean getNoteMode() {
        return false;
    }
}
