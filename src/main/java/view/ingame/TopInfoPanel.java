package view.ingame;

import util.Themes;

import javax.swing.*;
import java.awt.*;

public class TopInfoPanel extends JPanel {
    /**
     * Various {@link Color colors} for the game menu.
     */
    // Color for field-background
    protected Color panelBackgroundColor;
    // Color for text
    protected Color primaryTextColor;

    /**
     * The actual played gamemode.
     */
    protected static util.Mode gamemode;

    /**
     * A text that is shown in the menu when various events are triggered.
     */
    protected JLabel guiText = new JLabel("");;
    protected boolean textSet;

    public TopInfoPanel(String theme, util.Mode gamemode){
        Themes t = new Themes(theme);
        panelBackgroundColor = t.getPanelBackgroundColor();
        primaryTextColor = t.getPrimaryTextColor();
        this.setBackground(panelBackgroundColor);
        this.setBounds(20,20, 800,80);
        this.setLayout(new GridBagLayout());

        TopInfoPanel.gamemode = gamemode;

        textSet = false;

    }

    /**
     * Prints a text to the top of the gui
     *
     * @param text text to be printed
     * @param color color in which the text should be printed
     */
    public void setGUIText(String text, Color color) {
        guiText.setText(text);
        guiText.setOpaque(true);
        guiText.setBackground(panelBackgroundColor);
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
        this.setGUIText(text, primaryTextColor);
    }

    /**
     * If there is a text at the top of the gui this method will remove it
     */
    public void resetGUIText() {
        if (textSet) {
            this.removeAll();
            this.revalidate();
            textSet = false;
        }
    }
}
