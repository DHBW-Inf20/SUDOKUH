package view.ingame;

import view.main_menu.MainMenu;

import javax.swing.*;

import static javax.swing.JOptionPane.*;
import static util.Strings.*;

/**
 * @author Fabian Heinl
 */
public final class PopUpWindow {

    /**
     * Window to confirm backToMenu press
     * @param frame current open frame
     */
    public PopUpWindow(InGameViewScaffold frame) {
        int selectedValue = JOptionPane.showOptionDialog(null, GO_TO_MAIN_MENU_ALL_PROGRESS_WILL_BE_LOST, SUDOKUH, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{YES, NO}, NO);

        //Button Events
        if (selectedValue == YES_OPTION) {
            frame.dispose();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            new MainMenu();
        }
        if ((selectedValue == NO_OPTION || selectedValue == CLOSED_OPTION) && frame.getPlayPresenter() != null) {
            frame.getPlayPresenter().resumeTimer();
        }
    }
}
