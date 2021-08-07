package view.game_menus;

import view.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayMenu extends GameMenu {

    // Note-Mode active or not
    protected boolean noteMode;

    public PlayMenu(int size, ActionListener buttonListener, String title, String theme) {
        super(size, buttonListener, title, theme);
        noteMode = false;
    }

    // Definition of pre-defined elements -> cannot be changed
    public void setPredefined(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setPredefined(true);
        labels.get(row).get(col).setBackground(predefinedColor);
        labels.get(row).get(col).setForeground(primaryTextColor);
    }

    @Override
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        CustomButton buttonTip = new CustomButton(CustomButton.Type.TIP);
        buttonTip.setForeground(primaryTextColor);
        buttonTip.setBackground(primaryBackgroundColor);
        buttonsPanel.add(buttonTip);
        buttonTip.addActionListener(buttonListener);
        CustomButton buttonVerify = new CustomButton(CustomButton.Type.VERIFY);
        buttonVerify.setForeground(primaryTextColor);
        buttonVerify.setBackground(primaryBackgroundColor);
        buttonsPanel.add(buttonVerify);
        buttonVerify.addActionListener(buttonListener);
        CustomButton buttonPen = new CustomButton(CustomButton.Type.PEN);
        buttonPen.setForeground(primaryTextColor);
        buttonPen.setBackground(primaryBackgroundColor);
        buttonsPanel.add(buttonPen);
        buttonPen.addActionListener(buttonListener);
    }

    @Override
    // Delete a value from a cell
    public void resetCell() {
        clicked.setText("");
    }

    public void changeNoteMode(CustomButton button) {
        noteMode = !noteMode;
        if (noteMode) {
            button.setBackground(Color.green);
            clicked.setNoteMode();
        } else {
            button.setBackground(primaryBackgroundColor);
            clicked.setNormalMode();
        }
    }

    public boolean getNoteMode() {
        return noteMode;
    }

    public void setNote(int value) {
        clicked.setNote(value);
        pane.revalidate();
    }
}
