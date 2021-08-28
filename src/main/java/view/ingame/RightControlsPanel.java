package view.ingame;

import util.GameMode;
import view.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static view.ingame.CustomButton.Type.*;

/**
 * @author Philipp Kremling
 * @author Fabian Heinl
 */
public final class RightControlsPanel extends JPanel {

    private final Theme theme;

    CustomButton buttonNote, buttonChooseGroup, buttonEditGroup;

    /**
     * The actual played gamemode.
     */
    private final GameMode gamemode;

    private CustomButton buttonTip;

    public RightControlsPanel(int gridSize, ActionListener buttonListener, Theme theme, GameMode gamemode){
        this.setBounds(840, 120,320,600);

        this.theme = theme;

        this.gamemode = gamemode;

        //Layout for buttons
        GridLayout layout = new GridLayout(gridSize + 1, gridSize);
        layout.setHgap(12);
        layout.setVgap(12);
        this.setLayout(layout);
        // Buttons for input
        setBackground(theme.normalCellColor);
        setForeground(theme.primaryTextColor);
        for (int i = 1; i <= gridSize * gridSize; i++) {
            CustomButton button = new CustomButton(NUMBER, i);
            button.setForeground(theme.primaryTextColor);
            button.setBackground(theme.normalCellColor);
            button.setOpaque(true);
            this.add(button);
            button.addActionListener(buttonListener);
        }
        CustomButton buttonDelete = new CustomButton(DELETE);
        buttonDelete.setForeground(theme.primaryTextColor);
        buttonDelete.setBackground(theme.normalCellColor);
        buttonDelete.setOpaque(true);
        this.add(buttonDelete);
        buttonDelete.addActionListener(buttonListener);
        buttonNote = new CustomButton(NOTE);
        setCustomButtons(buttonListener);

        setBackground(theme.menuBackgroundColor);
    }

    /**
     * Add buttons other than the standard ones to the menu
     *
     * @param buttonListener The listener the buttons should be attached to
     */
    public void setCustomButtons(ActionListener buttonListener) {
        switch(gamemode) {
            case STR8TS_SOLVE -> {
                CustomButton buttonSolve = new CustomButton(SOLVE);
                buttonSolve.setForeground(theme.primaryTextColor);
                buttonSolve.setBackground(theme.normalCellColor);
                buttonSolve.setOpaque(true);
                this.add(buttonSolve);
                buttonSolve.addActionListener(buttonListener);
                CustomButton buttonChangeColor = new CustomButton(CHANGE_COLOR);
                buttonChangeColor.setForeground(theme.primaryTextColor);
                buttonChangeColor.setBackground(theme.normalCellColor);
                buttonChangeColor.setOpaque(true);
                this.add(buttonChangeColor);
                buttonChangeColor.addActionListener(buttonListener);
            }
            case SUDOKU_PLAY -> {
                buttonTip = new CustomButton(TIP);
                buttonTip.setForeground(theme.primaryTextColor);
                buttonTip.setBackground(theme.normalCellColor);
                buttonTip.setOpaque(true);
                this.add(buttonTip);
                buttonTip.addActionListener(buttonListener);
                buttonNote.setForeground(theme.primaryTextColor);
                buttonNote.setBackground(theme.normalCellColor);
                buttonNote.setOpaque(true);
                this.add(buttonNote);
                buttonNote.addActionListener(buttonListener);
                CustomButton buttonVerify = new CustomButton(VERIFY);
                buttonVerify.setForeground(theme.primaryTextColor);
                buttonVerify.setBackground(theme.normalCellColor);
                buttonVerify.setOpaque(true);
                this.add(buttonVerify);
                buttonVerify.addActionListener(buttonListener);
            }
            case KILLER_SOLVE -> {
                buttonChooseGroup = new CustomButton(CHOOSE_GROUP);
                buttonChooseGroup.setForeground(theme.primaryTextColor);
                buttonChooseGroup.setBackground(theme.normalCellColor);
                buttonChooseGroup.setOpaque(true);
                this.add(buttonChooseGroup);
                buttonChooseGroup.addActionListener(buttonListener);
                CustomButton buttonDeleteGroup = new CustomButton(REMOVE_GROUP);
                buttonDeleteGroup.setForeground(theme.primaryTextColor);
                buttonDeleteGroup.setBackground(theme.normalCellColor);
                buttonDeleteGroup.setOpaque(true);
                this.add(buttonDeleteGroup);
                buttonDeleteGroup.addActionListener(buttonListener);
                buttonEditGroup = new CustomButton(EDIT_GROUP);
                buttonEditGroup.setForeground(theme.primaryTextColor);
                buttonEditGroup.setBackground(theme.normalCellColor);
                buttonEditGroup.setOpaque(true);
                this.add(buttonEditGroup);
                buttonEditGroup.addActionListener(buttonListener);
                CustomButton buttonSolve = new CustomButton(SOLVE);
                buttonSolve.setForeground(theme.primaryTextColor);
                buttonSolve.setBackground(theme.normalCellColor);
                buttonSolve.setOpaque(true);
                this.add(buttonSolve);
                buttonSolve.addActionListener(buttonListener);
            }
            default -> {
                CustomButton buttonSolve = new CustomButton(SOLVE);
                buttonSolve.setForeground(theme.primaryTextColor);
                buttonSolve.setBackground(theme.normalCellColor);
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
        buttonNote.setBackground(Color.decode("#78B53A"));
    }

    /**
     * Sets the color of the note button to normal
     */
    public void setNormalMode() {
        buttonNote.setBackground(theme.normalCellColor);
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
        buttonChooseGroup.setBackground(theme.normalCellColor);
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
        buttonEditGroup.setBackground(theme.normalCellColor);
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
