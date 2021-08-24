package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

public class GroupPopUpWindow {
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
            System.out.println("Tried to set the sum to "+selectedValue);
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