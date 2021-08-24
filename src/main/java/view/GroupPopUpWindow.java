package view;

import view.ingame.InGameViewScaffold;

import javax.swing.*;

public class GroupPopUpWindow {
    int sum;

    public GroupPopUpWindow(InGameViewScaffold frame) {
        sum = 0;
        String selectedValue = JOptionPane.showInputDialog("Welche Summe hat diese Gruppe?");
        try {
            sum = Integer.parseInt(selectedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSum() {
        return sum;
    }
}
