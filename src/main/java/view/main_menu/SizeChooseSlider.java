package view.main_menu;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * @author Fabian Heinl
 */
public final class SizeChooseSlider extends JSlider {

    /**
     * Labels for the JSlider
     */
    private final Hashtable<Integer, JLabel> labels;

    public SizeChooseSlider() {
        setMinimum(2);
        setMaximum(4);
        setValue(3);
        setPaintTicks(true);
        setPaintLabels(true);
        labels = new Hashtable<>();
        labels.put(2, new JLabel("4"));
        labels.put(3, new JLabel("9"));
        labels.put(4, new JLabel("16"));
        setLabelTable(labels);
        setBounds(70, 325, 200, 50);
    }

    /**
     * Change label Color for dark mode
     * @param color new Color
     */
    public void setLabelColors(Color color) {
        labels.values().forEach(label -> label.setForeground(color));
    }
}
