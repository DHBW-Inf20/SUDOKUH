package presenter;

import view.Theme;
import view.ingame.InGameViewScaffold;

/**
 * @author Philipp Kremling
 */
public class SolveSudokuPresenter extends SolvePresenter {
    public SolveSudokuPresenter(int size, Theme theme, boolean autoStepForward, boolean highlighting) {
        super(size, util.Mode.SUDOKU_SOLVE, theme, autoStepForward, highlighting);
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }

    @Override
    public boolean getNoteMode() {
        return false;
    }
}
