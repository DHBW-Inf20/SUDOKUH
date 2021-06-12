import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        GUI gui =  new GUI(9);
        gui.setSize(800,600);
        gui.setVisible(true);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}