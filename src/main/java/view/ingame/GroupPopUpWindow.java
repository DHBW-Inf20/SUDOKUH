package view.ingame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

import static util.Strings.WHICH_SUM_DOES_THIS_GROUP_HAVE;

/**
 * @author Philipp Kremling
 */
public final class GroupPopUpWindow {
    int sum;

    public GroupPopUpWindow(ArrayList<CellPanel> group) {
        sum = -1;
        String selectedValue = JOptionPane.showInputDialog(WHICH_SUM_DOES_THIS_GROUP_HAVE);
        int minSum = 0;
        for (CellPanel cellPanel : group) {
            if (!Objects.equals(cellPanel.getLabelValue(), "") && cellPanel.getLabelValue() != null) {
                minSum += Integer.parseInt(cellPanel.getLabelValue());
            }
        }
        try {
            sum = Integer.parseInt(selectedValue);
            if (sum < minSum || sum == 0) sum = -2;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the sum of the cell got by user (in case of logical incorrect input: -2, if no or a false value: -1)
     */
    public int getSum() {
        return sum;
    }
}
