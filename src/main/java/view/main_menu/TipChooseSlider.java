package view.main_menu;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * @author Fabian Heinl
 * @author Luca Kellermann
 */
public final class TipChooseSlider extends JSlider {

    /**
     * Labels for the JSlider
     */
    private final Hashtable<Integer, JLabel> labels;

    public TipChooseSlider(int startValue) {
        setMinimum(0);
        setMaximum(4);
        switch (startValue){
            case 0 -> setValue(0);
            case 3 -> setValue(1);
            case 5 -> setValue(2);
            case 10 -> setValue(3);
            case 20 -> setValue(4);
        }
        setPaintTicks(true);
        setPaintLabels(true);
        labels = new Hashtable<>();
        labels.put(0, new JLabel("0"));
        labels.put(1, new JLabel("3"));
        labels.put(2, new JLabel("5"));
        labels.put(3, new JLabel("10"));
        labels.put(4, new JLabel("20"));
        setLabelTable(labels);
        setBounds(100, 500, 150, 50);
    }

    /**
     * Change label Color for dark mode
     * @param color new Color
     */
    public void setLabelColors(Color color) {
        labels.values().forEach(label -> label.setForeground(color));
    }
}
