package view.ingame;

import presenter.PlayPresenter;
import presenter.SolveKillerPresenter;
import presenter.SolveStr8tsPresenter;
import presenter.SolveSudokuPresenter;
import util.GameMode;
import view.Theme;

import javax.swing.*;

import static javax.swing.JOptionPane.*;
import static util.Strings.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public final class AgainPopUpWindow {

    /**
     * Window to confirm restart press.
     * <p>
     * Takes the current {@code frame} to close and params to restart the game with the same settings again.
     */
    public AgainPopUpWindow(InGameViewScaffold frame, GameMode gamemode, int gridSize, Theme theme,
                            boolean autoStepForward, boolean highlighting, int tipLimit) {
        int selectedValue = JOptionPane.showOptionDialog(null, RESET_ALL_PROGRESS_WILL_BE_LOST, SUDOKUH,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{YES, NO}, NO);

        // Button Events
        if (selectedValue == YES_OPTION) {
            frame.dispose();
            switch (gamemode) {
                case SUDOKU_SOLVE -> new SolveSudokuPresenter(gridSize, theme, autoStepForward, highlighting);
                case SUDOKU_PLAY -> new PlayPresenter(gridSize, theme, autoStepForward, highlighting, tipLimit);
                case KILLER_SOLVE -> new SolveKillerPresenter(gridSize, theme, autoStepForward, highlighting);
                case STR8TS_SOLVE -> new SolveStr8tsPresenter(gridSize, theme, autoStepForward, highlighting);
            }
        } else if ((selectedValue == NO_OPTION || selectedValue == CLOSED_OPTION) && frame.getPlayPresenter() != null) {
            frame.getPlayPresenter().resumeTimer();
        }
    }
}
