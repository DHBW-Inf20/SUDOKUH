package view;

import view.ingame.InGameViewScaffold;
import view.main_menu.MainMenu;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import static javax.swing.JOptionPane.*;


public class PopUpWindow {

    public PopUpWindow(InGameViewScaffold frame) {
        int selectedValue = JOptionPane.showOptionDialog(null, "Zurück zum Menü? Der Fortschritt geht verloren!", "SUDOKUH", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"JA", "NEIN"},"NEIN");

        //Button Events
        if(selectedValue == YES_OPTION){
            frame.dispose();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception E) {}
            new MainMenu();
        }
        if((selectedValue == NO_OPTION || selectedValue == CLOSED_OPTION)&& frame.getPlayPresenter() != null){
            frame.getPlayPresenter().resumeTimer();
        }
    }
}
