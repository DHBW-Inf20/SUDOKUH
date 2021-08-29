package view.ingame;

import javax.swing.*;

import static util.Strings.WHICH_SUM_DOES_THIS_GROUP_HAVE;

/**
 * @author Philipp Kremling
 */
public final class GroupPopUpWindow {
    private int sum = -1;

    public GroupPopUpWindow() {
        String selectedValue = JOptionPane.showInputDialog(WHICH_SUM_DOES_THIS_GROUP_HAVE);
        try {
            sum = Integer.parseInt(selectedValue);
        } catch (Exception ignored) {}
    }

    /**
     * @return the sum of the cell got by user (if wrong or no value: -1)
     */
    public int getSum() {
        return sum;
    }
}
