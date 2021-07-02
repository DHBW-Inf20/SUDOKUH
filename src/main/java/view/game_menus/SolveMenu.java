package view.game_menus;

import java.awt.*;
import java.awt.event.ActionListener;

public class SolveMenu extends GameMenu {

    public SolveMenu(int size, ActionListener buttonListener, String title) {
        super(size, buttonListener, title);
    }

    @Override
    public void validInput(String input) {
        clicked.setText(input);
        clicked.setForeground(Color.black);
        clicked.getLabel().setForeground(Color.black);
        invalid = null;
    }
}
