package view.ingame;

import view.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public class TopInfoPanel extends JPanel {

    private final Theme theme;

    /**
     * A text that is shown in the menu when various events are triggered.
     */
    private final JLabel guiText = new JLabel("");
    private boolean textSet;

    public TopInfoPanel(Theme theme) {
        this.theme = theme;

        setBackground(theme.panelBackgroundColor);
        this.setBounds(20, 20, 800, 80);
        this.setLayout(new GridBagLayout());

        textSet = false;
    }

    /**
     * Prints a text to the top of the gui
     *
     * @param text  text to be printed
     * @param color color in which the text should be printed
     */
    public void setGUIText(String text, Color color) {
        guiText.setText(text);
        guiText.setOpaque(true);
        guiText.setBackground(theme.panelBackgroundColor);
        guiText.setForeground(color);
        guiText.setFont(new Font(getFont().getName(), Font.BOLD, 25));
        guiText.setHorizontalAlignment(SwingConstants.CENTER);
        guiText.setVerticalAlignment(SwingConstants.CENTER);
        this.removeAll();
        this.add(guiText);
        this.validate();
        textSet = true;
    }

    /**
     * Prints a text to the top of the gui in the primary text color
     *
     * @param text text to be printed
     */
    public void setGUIText(String text) {
        this.setGUIText(text, theme.primaryTextColor);
    }

    /**
     * If there is a text at the top of the gui this method will remove it
     */
    public void resetGUIText() {
        if (textSet) {
            this.removeAll();
            this.repaint();
            this.revalidate();
            textSet = false;
        }
    }
}
