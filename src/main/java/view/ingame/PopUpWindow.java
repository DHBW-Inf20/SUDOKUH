package view.ingame;

import view.main_menu.MainMenu;

import javax.swing.*;

import static javax.swing.JOptionPane.*;

/**
 * @author Fabian Heinl
 */
public class PopUpWindow {

    public PopUpWindow(InGameViewScaffold frame) {
        int selectedValue = JOptionPane.showOptionDialog(null, "Zur\u00fcck zum Men\u00fc? Der Fortschritt geht verloren!", "SUDOKUH", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"JA", "NEIN"}, "NEIN");

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
