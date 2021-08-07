package presenter;

public class SolveSudokuPresenter  extends SolvePresenter {
    public SolveSudokuPresenter(int size) {
        super(size, util.Mode.SUDOKU_SOLVE);
    }
}
