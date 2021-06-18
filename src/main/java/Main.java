import view.MainGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        MainGUI gui = new MainGUI();
        gui.setSize(800,600);
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
