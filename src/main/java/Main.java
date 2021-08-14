import view.ingame.InGameViewScaffold;
import view.main_menu.MainMenu;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception E) {}

        MainMenu menu = new MainMenu();
        menu.setLocationRelativeTo(null);
    }
}
