import view.GUI;
import view.main_menu.MainMenu;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // For test-cases TODO choose style
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception E) {}

        MainMenu menu = new MainMenu();
        menu.setLocationRelativeTo(null);
//        GUI gui = new GUI(9);
//        gui.setSize(800,600);
//        gui.setVisible(true);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
