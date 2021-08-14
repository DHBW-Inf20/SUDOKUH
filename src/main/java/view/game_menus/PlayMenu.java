package view.game_menus;

import view.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashSet;

import static util.Type.*;

public class PlayMenu extends GameMenu {

    /**
     * Specifies whether the node mode is active or not
     */
    protected boolean noteMode;

    protected CustomButton buttonTip;

    public PlayMenu(int size, ActionListener buttonListener, String title, String theme) {
        super(size, buttonListener, title, theme);
        noteMode = false;
    }

    /**
     * Marks a specific cell as predifined in order to cannot be changed and has another color
     */
    public void setPredefined(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setPredefined(true);
        labels.get(row).get(col).setBackground(predefinedColor);
        labels.get(row).get(col).setForeground(primaryTextColor);
    }

    /**
     * Add buttons other than the standard ones to the menu
     *
     * @param buttonsPanel The panel where the buttons should be added to
     * @param buttonListener The listener the buttons should be attached to
     */
    @Override
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        buttonTip = new CustomButton(TIP);
        buttonTip.setForeground(primaryTextColor);
        buttonTip.setBackground(primaryBackgroundColor);
        buttonTip.setOpaque(true);
        buttonsPanel.add(buttonTip);
        buttonTip.addActionListener(buttonListener);
        CustomButton buttonVerify = new CustomButton(VERIFY);
        buttonVerify.setForeground(primaryTextColor);
        buttonVerify.setBackground(primaryBackgroundColor);
        buttonVerify.setOpaque(true);
        buttonsPanel.add(buttonVerify);
        buttonVerify.addActionListener(buttonListener);
        CustomButton buttonPen = new CustomButton(PEN);
        buttonPen.setForeground(primaryTextColor);
        buttonPen.setBackground(primaryBackgroundColor);
        buttonPen.setOpaque(true);
        buttonsPanel.add(buttonPen);
        buttonPen.addActionListener(buttonListener);
    }

    /**
     * Deletes the value of the actual clicked cell
     */
    @Override
    public void resetCell() {
        clicked.setText("");
    }

    /**
     * Changes the node mode and sets the color of the button
     */
    public void changeNoteMode(CustomButton button) {
        noteMode = !noteMode;
        if (noteMode) {
            button.setBackground(Color.decode("#78B53A"));
            clicked.setNoteMode();
        } else {
            button.setBackground(primaryBackgroundColor);
            clicked.setNormalMode();
        }
    }

    /**
     * Sets a valid input to cell with changing the tip-button to the remaining tips
     *
     * @param input the input value from type integer
     * @param tipsRemaining the number how many tips are remaining
     */
    public void validInput(String input, int tipsRemaining) {
        clicked.setText(input);
        clicked.setForeground(primaryTextColor);
        clicked.getLabel().setForeground(primaryTextColor);
        invalid = null;
        if(! conflicts.isEmpty()) {
            for(model.AbstractPuzzle.Cell c : conflicts) {
                labels.get(c.row()).get(c.column()).getLabel().setForeground(primaryTextColor);
            }
        }
        conflicts = new HashSet<>();
        buttonTip.setText("Tipp anzeigen ("+Integer.valueOf(tipsRemaining)+")");
    }

    /**
     * Changing the tip-button to the remaining tips
     *
     * @param tipsRemaining the number how many tips are remaining
     */
    public void setRemainingTips(int tipsRemaining) {
        buttonTip.setText("Tipp anzeigen ("+Integer.valueOf(tipsRemaining)+")");
    }

    /**
     * Changing the color of the tip-button when there are no more remaining tips
     */
    public void reachedMaxTips() {
        buttonTip.setBackground(Color.RED);
    }

    /**
     * @return the actual set note mode
     */
    public boolean getNoteMode() {
        return noteMode;
    }

    /**
     * Sets a note to the clicked cell
     */
    public void setNote(int value) {
        clicked.setNote(value);
        pane.revalidate();
    }
}
