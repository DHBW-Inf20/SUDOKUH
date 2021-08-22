package presenter;

import util.Mode;
import view.ingame.InGameViewScaffold;

public class SolveKillerPresenter extends SolvePresenter {

    public SolveKillerPresenter(int size, String theme, boolean autoStepForward, boolean highlighting) {
        super(size, util.Mode.KILLER_SOLVE, theme, highlighting, autoStepForward);
    }

    public InGameViewScaffold getInGameViewScaffold() {
        return inGameViewScaffold;
    }
}
