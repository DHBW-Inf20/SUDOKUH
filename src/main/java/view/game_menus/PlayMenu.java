package view.game_menus;

import view.CustomButton;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PlayMenu extends GameMenu {
    public PlayMenu(int size, ActionListener buttonListener, String title) {
        super(size, buttonListener, title);
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
}
