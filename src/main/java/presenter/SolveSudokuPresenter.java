package presenter;

import util.GameMode;
import view.Theme;
import view.ingame.InGameViewScaffold;

/**
 * @author Philipp Kremling
 */
public final class SolveSudokuPresenter extends SolvePresenter {
    public SolveSudokuPresenter(int gridSize, Theme theme, boolean autoStepForward, boolean highlighting) {
        super(gridSize, GameMode.SUDOKU_SOLVE, theme, autoStepForward, highlighting);
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }

    @Override
    public boolean isNoteModeActivated() {
        return false;
    }
}
