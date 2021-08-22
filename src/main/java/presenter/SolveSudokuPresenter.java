package presenter;

import util.Mode;
import view.ingame.InGameViewScaffold;

public class SolveSudokuPresenter  extends SolvePresenter {
    public SolveSudokuPresenter(int size, String theme, boolean autoStepForward, boolean  highlighting) {
        super(size, util.Mode.SUDOKU_SOLVE, theme, autoStepForward, highlighting);
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }
}
