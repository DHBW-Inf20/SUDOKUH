package view;

import view.ingame.InGameViewScaffold;

import javax.swing.*;

import static javax.swing.JOptionPane.*;

public class AgainPopUpWindow {

    public AgainPopUpWindow(InGameViewScaffold frame, util.Mode gamemode, int size, String theme, boolean autoStepForward, boolean highlighting, int tipLimit) {
        int selectedValue = JOptionPane.showOptionDialog(null, "Spielfeld zurücksetzen? Der Fortschritt geht verloren!", "SUDOKUH", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"JA", "NEIN"},"NEIN");

        //Button Events
        if(selectedValue == YES_OPTION){
            frame.dispose();
            switch (gamemode) {
                case SUDOKU_SOLVE -> new presenter.SolveSudokuPresenter(size, theme, autoStepForward, highlighting);
                case SUDOKU_PLAY -> new presenter.PlayPresenter(size, theme, autoStepForward, highlighting, tipLimit);
                case KILLER_SOLVE -> new presenter.SolveKillerPresenter(size, theme, autoStepForward, highlighting);
                case STRAITS_SOLVE -> new presenter.SolveStr8tsPresenter(size, theme, autoStepForward, highlighting);
            }
        }
        if((selectedValue == NO_OPTION || selectedValue == CLOSED_OPTION)&& frame.getPlayPresenter() != null){
            frame.getPlayPresenter().resumeTimer();
        }
    }
}
