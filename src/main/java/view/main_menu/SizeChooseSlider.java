package view.main_menu;

import javax.swing.*;
import java.util.Hashtable;

public class SizeChooseSlider extends JSlider{

    public SizeChooseSlider() {
        setMinimum(2);
        setMaximum(4);
        setValue(3);
        setPaintTicks(true);
        setPaintLabels(true);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(2, new JLabel("4"));
        labels.put(3, new JLabel("9"));
        labels.put(4, new JLabel("16"));
        setLabelTable(labels);
        setBounds(70, 325, 200, 50);
    }
}
