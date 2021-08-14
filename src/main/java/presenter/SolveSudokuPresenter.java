package presenter;

public class SolveSudokuPresenter  extends SolvePresenter {
    public SolveSudokuPresenter(int size, String theme, boolean autoStepForward) {
        super(size, util.Mode.SUDOKU_SOLVE, theme, autoStepForward);
    }
}
