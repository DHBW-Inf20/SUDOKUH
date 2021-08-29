import view.main_menu.MainMenu;

import javax.swing.*;

public final class Main {

    public static void main(String[] args) {
        //Sets LookAndFeel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        //Starts new Menu
        new MainMenu();
    }
}
