package view.game_menus;

import view.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayMenu extends GameMenu {

    // Note-Mode active or not
    protected boolean noteMode;

    public PlayMenu(int size, ActionListener buttonListener, String title) {
        super(size, buttonListener, title);
        noteMode = false;
    }

    // Definition of pre-defined elements -> cannot be changed
    public void setPredefined(int row, int col, int value) {
        labels.get(row).get(col).setText(Integer.toString(value));
        labels.get(row).get(col).setPredefined(true);
        labels.get(row).get(col).setBackground(predefinedColor);
    }

    @Override
    public void setCustomButtons(JPanel buttonsPanel, ActionListener buttonListener) {
        CustomButton buttonTip = new CustomButton(CustomButton.Type.TIP);
        buttonsPanel.add(buttonTip);
        buttonTip.addActionListener(buttonListener);
        CustomButton buttonVerify = new CustomButton(CustomButton.Type.VERIFY);
        buttonsPanel.add(buttonVerify);
        buttonVerify.addActionListener(buttonListener);
        CustomButton buttonPen = new CustomButton(CustomButton.Type.PEN);
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
            button.setBackground(Color.white);
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
