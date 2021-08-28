package view.ingame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Philipp Kremling
 */
public final class GroupPopUpWindow {
    int sum;

    public GroupPopUpWindow(ArrayList<LabelPanel> group) {
        sum = -1;
        String selectedValue = JOptionPane.showInputDialog("Welche Summe hat diese Gruppe?");
        int minSum = 0;
        for(LabelPanel l : group) {
            if(!Objects.equals(l.getLabelValue(), "") && l.getLabelValue() != null) minSum += Integer.parseInt(l.getLabelValue());
        }
        try {
            sum = Integer.parseInt(selectedValue);
            if(sum < minSum || sum == 0) sum = -2;
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
