package presenter;

import view.CustomButton;
import view.game_menus.GameMenu;

import java.awt.event.ActionEvent;

public interface Presenter {

    void handleButtonListenerEvent(ActionEvent e);
    void handleButton(CustomButton button);
    GameMenu getGameMenu();
}
