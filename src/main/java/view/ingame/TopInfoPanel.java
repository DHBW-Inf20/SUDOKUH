package view.ingame;

import view.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public final class TopInfoPanel extends JPanel {

    private final Theme theme;

    /**
     * A text that is shown in the menu when various events are triggered.
     */
    private final JLabel infoText = new JLabel("");
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
    public void setInfoText(String text, Color color) {
        infoText.setText(text);
        infoText.setOpaque(true);
        infoText.setBackground(theme.panelBackgroundColor);
        infoText.setForeground(color);
        infoText.setFont(new Font(getFont().getName(), Font.BOLD, 25));
        infoText.setHorizontalAlignment(SwingConstants.CENTER);
        infoText.setVerticalAlignment(SwingConstants.CENTER);
        this.removeAll();
        this.add(infoText);
        this.validate();
        textSet = true;
    }

    /**
     * Prints a text to the top of the gui in the primary text color
     *
     * @param text text to be printed
     */
    public void setInfoText(String text) {
        this.setInfoText(text, theme.primaryTextColor);
    }

    /**
     * If there is a text at the top of the gui this method will remove it
     */
    public void resetInfoText() {
        if (textSet) {
            this.removeAll();
            this.repaint();
            this.revalidate();
            textSet = false;
        }
    }
}
