package presenter;

import model.Str8ts;

public class PlayStr8tsPresenter extends PlayPresenter {
    public PlayStr8tsPresenter(int size) {
        super(size, "Str8ts");

        this.setColoredCells();
    }

    private void setColoredCells() {
        for (int i = 0; i < sudoku.getGridSize(); i++) {
            for (int j = 0; j < sudoku.getGridSize(); j++) {
                Str8ts.Color color = ((Str8ts) sudoku).getColor(i, j);
                if (color != Str8ts.Color.BLACK) {
                    gameMenu.setColor(i, j, color);
                }
            }
        }
    }
}