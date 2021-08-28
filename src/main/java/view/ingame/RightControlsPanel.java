package view.ingame;

import view.Themes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static util.Type.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public class RightControlsPanel extends JPanel {
    /**
     * Various {@link Color colors} for the game menu.
     */
    // Color for field-background
    protected Color primaryBackgroundColor;
    // Color for field-background (secondary)
    protected Color secondaryBackgroundColor;
    // Color for field-background when clicked
    protected Color clickedColor;
    // Color for field-background when there mustn't be a duplicate to clicked field
    protected Color markedColor;
    // Color for field-background when field is predefined
    protected Color predefinedColor;
    // Color for field-background when field is predefined and possible conflicting to field
    protected Color predefinedMarkedColor;
    // Color for field-borders
    protected Color borderColor;
    // Color for text
    protected Color primaryTextColor;
    // Color for text (secondary)
    protected Color secondaryTextColor;
    // Color for panel background
    private Color panelBackgroundColor;
    // Color for text if there is an error
    protected Color errorTextColor;

    CustomButton buttonPen, buttonChooseGroup, buttonEditGroup;

    /**
     * The actual played gamemode.
     */
    protected static util.Mode gamemode;

    protected CustomButton buttonTip;

    public RightControlsPanel(int gridSize, ActionListener buttonListener, String theme, util.Mode gamemode){
        this.setBounds(840, 120,320,600);

        RightControlsPanel.gamemode = gamemode;

        Themes t = new Themes(theme);
        primaryBackgroundColor = t.getPrimaryBackgroundColor();
        secondaryBackgroundColor = t.getSecondaryBackgroundColor();
        clickedColor = t.getClickedColor();
        markedColor = t.getMarkedColor();
        predefinedColor = t.getPredefinedColor();
        predefinedMarkedColor = t.getPredefinedMarkedColor();
        primaryTextColor = t.getPrimaryTextColor();
        secondaryTextColor = t.getSecondaryTextColor();
        errorTextColor = t.getErrorTextColor();
        borderColor = t.getBorderColor();
        panelBackgroundColor = t.getMenuBackgroundColor();

        //Layout for buttons
        GridLayout layout = new GridLayout(gridSize + 1, gridSize);
        layout.setHgap(12);
        layout.setVgap(12);
        this.setLayout(layout);
        // Buttons for input
        this.setBackground(primaryBackgroundColor);
        this.setForeground(primaryTextColor);
        for (int i = 1; i <= gridSize * gridSize; i++) {
            CustomButton button = new CustomButton(i, NUMBER);
            button.setForeground(primaryTextColor);
            button.setBackground(primaryBackgroundColor);
            button.setOpaque(true);
            this.add(button);
            button.addActionListener(buttonListener);
        }
        CustomButton buttonDelete = new CustomButton(DELETE);
        buttonDelete.setForeground(primaryTextColor);
        buttonDelete.setBackground(primaryBackgroundColor);
        buttonDelete.setOpaque(true);
        this.add(buttonDelete);
        buttonDelete.addActionListener(buttonListener);
        buttonPen = new CustomButton(PEN);
        setCustomButtons(buttonListener);

        this.setBackground(panelBackgroundColor);
    }

    /**
     * Add buttons other than the standard ones to the menu
     *
     * @param buttonListener The listener the buttons should be attached to
     */
    public void setCustomButtons(ActionListener buttonListener) {
        switch(gamemode) {
            case STRAITS_SOLVE -> {
                CustomButton buttonSolve = new CustomButton(SOLVE);
                buttonSolve.setForeground(primaryTextColor);
                buttonSolve.setBackground(primaryBackgroundColor);
                buttonSolve.setOpaque(true);
                this.add(buttonSolve);
                buttonSolve.addActionListener(buttonListener);
                CustomButton buttonChangeColor = new CustomButton(CHANGECOLOR);
                buttonChangeColor.setForeground(primaryTextColor);
                buttonChangeColor.setBackground(primaryBackgroundColor);
                buttonChangeColor.setOpaque(true);
                this.add(buttonChangeColor);
                buttonChangeColor.addActionListener(buttonListener);
            }
            case SUDOKU_PLAY -> {
                buttonTip = new CustomButton(TIP);
                buttonTip.setForeground(primaryTextColor);
                buttonTip.setBackground(primaryBackgroundColor);
                buttonTip.setOpaque(true);
                this.add(buttonTip);
                buttonTip.addActionListener(buttonListener);
                buttonPen.setForeground(primaryTextColor);
                buttonPen.setBackground(primaryBackgroundColor);
                buttonPen.setOpaque(true);
                this.add(buttonPen);
                buttonPen.addActionListener(buttonListener);
                CustomButton buttonVerify = new CustomButton(VERIFY);
                buttonVerify.setForeground(primaryTextColor);
                buttonVerify.setBackground(primaryBackgroundColor);
                buttonVerify.setOpaque(true);
                this.add(buttonVerify);
                buttonVerify.addActionListener(buttonListener);
            }
            case KILLER_SOLVE -> {
                buttonChooseGroup = new CustomButton(CHOOSEGROUP);
                buttonChooseGroup.setForeground(primaryTextColor);
                buttonChooseGroup.setBackground(primaryBackgroundColor);
                buttonChooseGroup.setOpaque(true);
                this.add(buttonChooseGroup);
                buttonChooseGroup.addActionListener(buttonListener);
                CustomButton buttonDeleteGroup = new CustomButton(REMOVEGROUP);
                buttonDeleteGroup.setForeground(primaryTextColor);
                buttonDeleteGroup.setBackground(primaryBackgroundColor);
                buttonDeleteGroup.setOpaque(true);
                this.add(buttonDeleteGroup);
                buttonDeleteGroup.addActionListener(buttonListener);
                buttonEditGroup = new CustomButton(EDITGROUP);
                buttonEditGroup.setForeground(primaryTextColor);
                buttonEditGroup.setBackground(primaryBackgroundColor);
                buttonEditGroup.setOpaque(true);
                this.add(buttonEditGroup);
                buttonEditGroup.addActionListener(buttonListener);
                CustomButton buttonSolve = new CustomButton(SOLVE);
                buttonSolve.setForeground(primaryTextColor);
                buttonSolve.setBackground(primaryBackgroundColor);
                buttonSolve.setOpaque(true);
                this.add(buttonSolve);
                buttonSolve.addActionListener(buttonListener);
            }
            default -> {
                CustomButton buttonSolve = new CustomButton(SOLVE);
                buttonSolve.setForeground(primaryTextColor);
                buttonSolve.setBackground(primaryBackgroundColor);
                buttonSolve.setOpaque(true);
                this.add(buttonSolve);
                buttonSolve.addActionListener(buttonListener);
            }
        }
    }

    /**
     * Sets the color of the note button to green
     */
    public void setNoteMode() {
        buttonPen.setBackground(Color.decode("#78B53A"));
    }

    /**
     * Sets the color of the note button to normal
     */
    public void setNormalMode() {
        buttonPen.setBackground(primaryBackgroundColor);
    }

    /**
     * Sets the color of the choose group button to green
     */
    public void setChooseMode() {
        buttonChooseGroup.setBackground(Color.decode("#78B53A"));
    }

    /**
     * Sets the color of the choose group button to normal
     */
    public void setNoChooseMode() {
        buttonChooseGroup.setBackground(primaryBackgroundColor);
    }

    /**
     * Sets the color of the edit group button to green
     */
    public void setEditMode() {
        buttonEditGroup.setBackground(Color.decode("#78B53A"));
    }

    /**
     * Sets the color of the edit group button to normal
     */
    public void setNoEditMode() {
        buttonEditGroup.setBackground(primaryBackgroundColor);
    }

    /**
     * Changes the text of the tip button
     *
     * @param text the text to be set
     */
    public void setTipButtonText(String text) {
        buttonTip.setText(text);
    }

    /**
     * Changing the tip-button to the remaining tips
     *
     * @param tipsRemaining the number how many tips are remaining
     */
    public void setRemainingTips(int tipsRemaining) {
        buttonTip.setText(Integer.valueOf(tipsRemaining).toString());
    }

    /**
     * Changing the color of the tip-button when there are no more remaining tips
     */
    public void reachedMaxTips() {
        buttonTip.setBackground(Color.RED);
    }
}
